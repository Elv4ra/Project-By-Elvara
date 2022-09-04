package com.ProjectByElvara.services;

import com.ProjectByElvara.dao.interfaces.IOrderDAO;
import com.ProjectByElvara.dao.interfaces.IProductDAO;
import com.ProjectByElvara.dto.Cart;
import com.ProjectByElvara.dto.ProductDTO;
import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.OrderProduct;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.additionalClasses.Category;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;
import com.ProjectByElvara.entities.additionalClasses.ProductStatus;
import com.ProjectByElvara.mapper.Mapper;
import com.ProjectByElvara.services.exceptions.ProductConstraintException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final IProductDAO productDAO;
    private final IOrderDAO orderDAO;
    private final Cart sessionUserCart;

    public ProductService(IProductDAO productDAO, IOrderDAO orderDAO, Cart sessionUserCart) {
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.sessionUserCart = sessionUserCart;
    }

    public ProductDTO getProductById(Integer id) throws SQLException {
        Optional<Product> maybeProduct = this.productDAO.findById(id);
        if (maybeProduct.isEmpty()) {
            return null;
        }
        return Mapper.convertToDTO(maybeProduct.get());
    }

    private ProductDTO getProductByNameTrademarkAndSize(String productName, String trademark, String size) throws SQLException {
        Optional<Product> maybeProduct = this.productDAO.findByNameTrademarkAndSize(productName, trademark, size);
        if (maybeProduct.isEmpty()) {
            return null;
        }
        return Mapper.convertToDTO(maybeProduct.get());
    }

    public List<ProductDTO> getAllProducts(Category category) throws SQLException {
        if (category == null) {
            return this.productDAO.findAll().stream().map(Mapper::convertToDTO).collect(Collectors.toList());
        }
        return this.productDAO.findAllByCategory(category).stream().map(Mapper::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO addNewProduct(ProductDTO productDTO) throws SQLException {
        ProductDTO existedProduct = this.getProductByNameTrademarkAndSize(productDTO.getProductName(),
                productDTO.getTrademark(), productDTO.getSize());
        if (existedProduct != null) {
            return null;
        }
        this.productDAO.save(Mapper.convertToEntity(productDTO));
        return Mapper.convertToDTO(this.productDAO.findByNameTrademarkAndSize(productDTO.getProductName(),
                productDTO.getTrademark(), productDTO.getSize()).orElseThrow(SQLException::new));
    }

    public ProductDTO addToCart(String productId, String productAmount) throws SQLException {
        Integer id = Integer.parseInt(productId);
        Long amount = Long.parseLong(productAmount);
        ProductDTO productInCart = this.sessionUserCart.hasProduct(id);
        ProductDTO productInStore = this.getProductById(id);
        if (productInCart != null) {
            if (productInCart.getAmount() + amount < (productInStore.getAmount() < 10 ? productInStore.getAmount() : 10)) {
                return this.sessionUserCart.addProduct(id, amount);
            }
            if ((productInStore.getAmount() < 10 ? productInStore.getAmount() : 10) - productInCart.getAmount() > 0) {
                return this.sessionUserCart.addProduct(id, (productInStore.getAmount() < 10 ? productInStore.getAmount() : 10) - productInCart.getAmount());
            }
            return null;
        }
        productInStore.setAmount(amount);
        this.sessionUserCart.addProduct(productInStore);
        return productInStore;
    }

    public ProductDTO removeFromCart(Integer productId) {
        return this.sessionUserCart.removeFromCart(productId);
    }

    public void updateProduct(ProductDTO updatedProduct) throws SQLException, ProductConstraintException {
        ProductDTO oldProduct = this.getProductByNameTrademarkAndSize(updatedProduct.getProductName(),
                updatedProduct.getTrademark(),
                updatedProduct.getSize());
        if (oldProduct != null && !Objects.equals(oldProduct.getId(), updatedProduct.getId())) {
            throw new ProductConstraintException();
        }
        this.productDAO.update(Mapper.convertToEntity(updatedProduct));
    }

    public void changeProductStatus(String id) throws SQLException {
        Product product = this.productDAO.findById(Integer.parseInt(id)).orElseThrow(SQLException::new);
        if (product.getProductStatus() == ProductStatus.ACTIVE) {
            this.cancelAllOrdersWithProduct(product);
        } else {
            product.setProductStatus(ProductStatus.ACTIVE);
            this.productDAO.update(product);
        }
    }

    private void cancelAllOrdersWithProduct(Product product) throws SQLException {
        for (Order order : this.orderDAO.findAllAcceptedByProduct(product.getId())) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product restoredProduct = this.productDAO.findById(orderProduct.getProduct().getId()).orElseThrow(SQLException::new);
                restoredProduct.setAmount(restoredProduct.getAmount() + orderProduct.getAmount());
                if (Objects.equals(orderProduct.getProduct().getId(), product.getId())) {
                    restoredProduct.setProductStatus(ProductStatus.ARCHIVED);
                }
                this.productDAO.update(restoredProduct);
            }
            order.setUpdateTime(new Timestamp(Instant.now().toEpochMilli()));
            order.setOrderStatus(OrderStatus.REJECTED);
            this.orderDAO.update(order);
        }
    }

    @PostConstruct
    private void databaseInit() {

    }
}
