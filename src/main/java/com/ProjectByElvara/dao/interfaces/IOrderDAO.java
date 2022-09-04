package com.ProjectByElvara.dao.interfaces;


import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IOrderDAO {
    Optional<Order> findById(Integer id) throws SQLException;

    List<Order> findAllByUserId(Integer userId) throws SQLException;

    List<Order> findAllByStatus(OrderStatus status) throws SQLException;

    List<Order> findAllByUserIdAndStatus(Integer userId, OrderStatus status) throws SQLException;

    List<Order> findAllAcceptedByProduct(Integer productId) throws SQLException;

    List<Order> findAll() throws SQLException;

    void save(Order order) throws SQLException;

    void update(Order order) throws SQLException;

    void delete(Order order) throws SQLException;
}
