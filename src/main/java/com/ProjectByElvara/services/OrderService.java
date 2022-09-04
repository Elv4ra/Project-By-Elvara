package com.ProjectByElvara.services;

import com.ProjectByElvara.dao.interfaces.IOrderDAO;
import com.ProjectByElvara.dao.interfaces.IProductDAO;
import com.ProjectByElvara.dao.interfaces.IUserDAO;
import com.ProjectByElvara.dto.*;
import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.OrderProduct;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.User;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;
import com.ProjectByElvara.mapper.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final IOrderDAO orderDAO;
    private final IUserDAO userDAO;
    private final IProductDAO productDAO;
    private final Cart sessionUserCart;

    public OrderService(IOrderDAO orderDAO, IUserDAO userDAO, IProductDAO productDAO, Cart cart) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.sessionUserCart = cart;
    }

    public OrderDTO getOrderById(Integer id) throws SQLException {
        Optional<Order> maybeOrder = this.orderDAO.findById(id);
        if (maybeOrder.isEmpty()) {
            return null;
        }
        return  Mapper.convertToDTO(maybeOrder.get());
    }

    public List<OrderDTO> getAllOrders(OrderStatus status, Integer id) throws SQLException {
        if (status != null && id != null) {
            return this.orderDAO.findAllByUserIdAndStatus(id, status).stream().map(Mapper::convertToDTO).collect(Collectors.toList());
        }
        if (status == null && id != null) {
            return this.orderDAO.findAllByUserId(id).stream().map(Mapper::convertToDTO).collect(Collectors.toList());
        }
        if (status != null) {
            return this.orderDAO.findAllByStatus(status).stream().map(Mapper::convertToDTO).collect(Collectors.toList());
        }
        return this.orderDAO.findAll().stream().map(Mapper::convertToDTO).collect(Collectors.toList());
    }

    public Integer addNewOrder(OrderDTO newOrder, Integer userId) throws SQLException {
        Order order = Mapper.convertToEntity(newOrder);
        User user = this.userDAO.findById(userId).orElseThrow(SQLException::new);
        order.addUser(user);
        for (ProductDTO productDTO : this.sessionUserCart.getProducts()) {
            Product product = this.productDAO.findById(productDTO.getId()).orElseThrow(SQLException::new);
            product.setAmount(product.getAmount() - productDTO.getAmount());
            this.productDAO.update(product);
            order.addOrderProduct(new OrderProduct(order, product, productDTO.getAmount()));
        }
        this.orderDAO.save(order);
        this.sessionUserCart.removeAll();
        return user.getId();
    }

    public void updateOrderStatus(Integer orderId, OrderStatus status) throws SQLException {
        Order order = this.orderDAO.findById(orderId).orElseThrow(SQLException::new);
        OrderStatus orderStatus = order.getOrderStatus();
        order.setOrderStatus(status);
        order.setUpdateTime(new Timestamp(Instant.now().toEpochMilli()));
        if (status == OrderStatus.CANCELLED || (status == OrderStatus.REJECTED &&
                (orderStatus == OrderStatus.ACCEPTED || orderStatus == OrderStatus.DONE))) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = this.productDAO.findById(orderProduct.getProduct().getId()).orElseThrow(SQLException::new);
                product.setAmount(product.getAmount() + orderProduct.getAmount());
                this.productDAO.update(product);
            }
        }
        if ((status == OrderStatus.ACCEPTED || status == OrderStatus.DONE)
                && orderStatus == OrderStatus.REJECTED) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = this.productDAO.findById(orderProduct.getProduct().getId()).orElseThrow(SQLException::new);
                product.setAmount(product.getAmount() - orderProduct.getAmount());
                this.productDAO.update(product);
            }
        }
        this.orderDAO.update(order);
    }
}
