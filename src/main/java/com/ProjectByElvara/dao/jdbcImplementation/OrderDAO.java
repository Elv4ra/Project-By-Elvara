package com.ProjectByElvara.dao.jdbcImplementation;



import com.ProjectByElvara.dao.interfaces.IOrderDAO;
import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.OrderProduct;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.User;
import com.ProjectByElvara.entities.additionalClasses.*;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Profile("JDBC")
@Repository
public class OrderDAO implements IOrderDAO {
    private final DriverManagerDataSource dataSource;

    public OrderDAO(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time from orders " +
                "left join users on orders.user_id = users.id " +
                "left join order_products on orders.id = order_products.order_id " +
                "left join products on order_products.product_id = products.id "
                + "ORDER BY order_status ASC, update_time DESC;");
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<Integer, Order> map = new LinkedHashMap<>();
        Order order = null;
        while (resultSet.next()) {
            if (!map.containsKey(resultSet.getInt("id"))) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
                map.put(order.getId(), order);
            } else {
                map.get(resultSet.getInt("id"))
                        .addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Order> findAllByUserId(Integer userId) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time FROM orders " +
                "LEFT JOIN users ON orders.user_id = users.id " +
                "LEFT JOIN order_products ON orders.id = order_products.order_id " +
                "LEFT JOIN products ON order_products.product_id = products.id "
                + "WHERE user_id = ? ORDER BY order_status ASC, update_time DESC;");
        Map<Integer, Order> map = new LinkedHashMap<>();
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        while (resultSet.next()) {
            if (!map.containsKey(resultSet.getInt("id"))) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
                map.put(order.getId(), order);
            } else {
                map.get(resultSet.getInt("id"))
                        .addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Order> findAllByStatus(OrderStatus status) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time from orders " +
                "left join users on orders.user_id = users.id " +
                "left join order_products on orders.id = order_products.order_id " +
                "left join products on order_products.product_id = products.id "
                + "WHERE order_status = ? ORDER BY order_status ASC, update_time DESC;");
        Map<Integer, Order> map = new LinkedHashMap<>();
        preparedStatement.setString(1, status.name());
        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        while (resultSet.next()) {
            if (!map.containsKey(resultSet.getInt("id"))) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
                map.put(order.getId(), order);
            } else {
                map.get(resultSet.getInt("id"))
                        .addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Order> findAllByUserIdAndStatus(Integer userId, OrderStatus status) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time from orders " +
                "left join users on orders.user_id = users.id " +
                "left join order_products on orders.id = order_products.order_id " +
                "left join products on order_products.product_id = products.id "
                + "WHERE user_id = ? and order_status = ? ORDER BY order_status ASC, update_time DESC;");
        Map<Integer, Order> map = new LinkedHashMap<>();
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, status.name());
        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        while (resultSet.next()) {
            if (!map.containsKey(resultSet.getInt("id"))) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
                map.put(order.getId(), order);
            } else {
                map.get(resultSet.getInt("id"))
                        .addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Order> findAllAcceptedByProduct(Integer productId) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time from orders " +
                "left join users on orders.user_id = users.id " +
                "left join order_products on orders.id = order_products.order_id " +
                "left join products on order_products.product_id = products.id "
                + "WHERE product_id = ? and order_status = 'ACCEPTED' ORDER BY order_status ASC, update_time DESC;");
        Map<Integer, Order> map = new HashMap<>();
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        while (resultSet.next()) {
            if (!map.containsKey(resultSet.getInt("id"))) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
                map.put(order.getId(), order);
            } else {
                map.get(resultSet.getInt("id"))
                        .addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<Order> findById(Integer id) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select orders.id, " +
                "user_id, email, user_password, first_name, last_name, phone, user_role, " +
                "product_id, product_name, trademark, price, category, size, product_amount, product_status, " +
                "payment, delivery, address, order_status, update_time from orders " +
                "left join users on orders.user_id = users.id " +
                "left join order_products on orders.id = order_products.order_id " +
                "left join products on order_products.product_id = products.id "
                + "WHERE orders.id = ?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        while (resultSet.next()) {
            if (order == null) {
                order = this.createOrderEntity(resultSet);
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
            } else {
                order.addOrderProduct(this.createOrderProductEntity(order, resultSet));
            }
        }
        resultSet.close();
        preparedStatement.close();
        return Optional.ofNullable(order);
    }

    private Order createOrderEntity(ResultSet resultSet) throws SQLException {
        return new Order(resultSet.getInt("id"),
                this.createUserEntity(resultSet),
                Payment.valueOfCode(resultSet.getString("payment")),
                Delivery.valueOfCode(resultSet.getString("delivery")),
                resultSet.getString("address"),
                OrderStatus.valueOfCode(resultSet.getString("order_status")),
                resultSet.getTimestamp("update_time"));
    }

    private User createUserEntity(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("user_id"),
                resultSet.getString("email"),
                resultSet.getString("user_password"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone"),
                UserRole.valueOfCode(resultSet.getString("user_role")));
    }

    private OrderProduct createOrderProductEntity(Order order, ResultSet resultSet) throws SQLException {
        return new OrderProduct(order,
                this.createProductEntity(resultSet),
                resultSet.getLong("product_amount"));
    }

    private Product createProductEntity(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getInt("product_id"),
                resultSet.getString("product_name"),
                resultSet.getString("trademark"),
                resultSet.getDouble("price"),
                Category.valueOfCode(resultSet.getString("category")),
                resultSet.getString("size"),
                resultSet.getLong("product_amount"),
                ProductStatus.valueOfCode(resultSet.getString("product_status")));
    }

    @Override
    public void save(Order order) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "
                + "orders(user_id, payment, delivery, address, order_status, update_time) VALUES(?,?,?,?,?,?);");
        preparedStatement.setInt(1, order.getUser().getId());
        preparedStatement.setString(2, order.getPayment().name());
        preparedStatement.setString(3, order.getDelivery().name());
        preparedStatement.setString(4, order.getAddress());
        preparedStatement.setString(5, order.getOrderStatus().name());
        preparedStatement.setTimestamp(6, order.getUpdateTime());
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("INSERT INTO order_products(order_id, "
                + "product_id, product_amount) VALUES (LAST_INSERT_ID(),?,?);");
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            preparedStatement.setInt(1, orderProduct.getProduct().getId());
            preparedStatement.setLong(2, orderProduct.getAmount());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    @Override
    public void update(Order order) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders "
                + "SET user_id = ?, payment = ?, delivery = ?, address = ?, order_status = ?, "
                + "update_time = CURRENT_TIMESTAMP  WHERE id = ?;");
        preparedStatement.setInt(1, order.getUser().getId());
        preparedStatement.setString(2, order.getPayment().name());
        preparedStatement.setString(3, order.getDelivery().name());
        preparedStatement.setString(4, order.getAddress());
        preparedStatement.setString(5, order.getOrderStatus().name());
        preparedStatement.setInt(6, order.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public void delete(Order order) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM order_products "
                + "WHERE order_id = ?;");
        preparedStatement.setInt(1, order.getId());
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement("DELETE FROM orders WHERE id = ?;");
        preparedStatement.setInt(1, order.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
}
