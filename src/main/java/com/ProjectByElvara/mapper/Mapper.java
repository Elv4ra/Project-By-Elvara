package com.ProjectByElvara.mapper;

import com.ProjectByElvara.dto.OrderDTO;
import com.ProjectByElvara.dto.ProductDTO;
import com.ProjectByElvara.dto.UserDTO;
import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.User;

import java.util.stream.Collectors;

public class Mapper {
    public static UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(),
                user.getEmail(),
                user.getUserPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getUserRole());
    }

    public static User convertToEntity(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getUserPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPhone(),
                userDTO.getUserRole());
    }

    public static ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getProductName(),
                product.getTrademark(),
                product.getPrice(),
                product.getCategory(),
                product.getSize(),
                product.getAmount(),
                product.getProductStatus());
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getId(),
                productDTO.getProductName(),
                productDTO.getTrademark(),
                productDTO.getPrice(),
                productDTO.getCategory(),
                productDTO.getSize(),
                productDTO.getAmount(),
                productDTO.getStatus());
    }

    public static OrderDTO convertToDTO(Order order) {
        return new OrderDTO(order.getId(),
                Mapper.convertToDTO(order.getUser()),
                order.getOrderProducts().stream().map(orderProduct -> {
                    ProductDTO productDTO = convertToDTO(orderProduct.getProduct());
                    productDTO.setAmount(orderProduct.getAmount());
                    return productDTO;
                }).collect(Collectors.toList()),
                order.getPayment(),
                order.getDelivery(),
                order.getAddress(),
                order.getOrderStatus(),
                order.getUpdateTime());
    }

    public static Order convertToEntity(OrderDTO orderDTO) {
        return new Order(orderDTO.getId(),
                orderDTO.getUser() == null ? null : Mapper.convertToEntity(orderDTO.getUser()),
                orderDTO.getPayment(),
                orderDTO.getDelivery(),
                orderDTO.getAddress(),
                orderDTO.getStatus(),
                orderDTO.getUpdateTime());
    }
}
