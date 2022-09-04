package com.ProjectByElvara.dao.jdbcImplementation;


import com.ProjectByElvara.dao.interfaces.IProductDAO;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.additionalClasses.Category;
import com.ProjectByElvara.entities.additionalClasses.ProductStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("JDBC")
@Repository
public class ProductDAO implements IProductDAO {
    private final DriverManagerDataSource dataSource;

    public ProductDAO(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products;");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = this.createProductEntity(resultSet);
            products.add(product);
        }
        resultSet.close();
        preparedStatement.close();
        return products;
    }

    @Override
    public List<Product> findAllByCategory(Category category) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products "
                + "WHERE category = ?;");
        preparedStatement.setString(1, category.name());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = this.createProductEntity(resultSet);
            products.add(product);
        }
        resultSet.close();
        preparedStatement.close();
        return products;
    }

    @Override
    public Optional<Product> findById(Integer id) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products "
                + "WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Product product = null;
        if (resultSet.next()) {
            product = this.createProductEntity(resultSet);
        }
        resultSet.close();
        preparedStatement.close();
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> findByNameTrademarkAndSize(String productName, String trademark, String size) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products "
                + "WHERE product_name = ? AND trademark = ? AND size = ?;");
        preparedStatement.setString(1, productName);
        preparedStatement.setString(2, trademark);
        preparedStatement.setString(3, size);
        ResultSet resultSet = preparedStatement.executeQuery();
        Product product = null;
        if (resultSet.next()) {
            product = this.createProductEntity(resultSet);
        }
        resultSet.close();
        preparedStatement.close();
        return Optional.ofNullable(product);
    }

    private Product createProductEntity(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getInt("id"),
                resultSet.getString("product_name"),
                resultSet.getString("trademark"),
                resultSet.getDouble("price"),
                Category.valueOfCode(resultSet.getString("category")),
                resultSet.getString("size"),
                resultSet.getLong("amount"),
                ProductStatus.valueOfCode(resultSet.getString("product_status")));
    }

    @Override
    public void save(Product product) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "
                + "products(product_name, trademark,price, category, size, amount, product_status) "
                + "VALUE (?,?,?,?,?,?,?)");
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setString(2, product.getTrademark());
        preparedStatement.setDouble(3, product.getPrice());
        preparedStatement.setString(4, product.getCategory().name());
        preparedStatement.setString(5, product.getSize());
        preparedStatement.setLong(6, product.getAmount());
        preparedStatement.setString(7, product.getProductStatus().name());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public void update(Product product) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products "
                + "SET product_name = ?, trademark = ?, price = ?, category = ?, size = ?, "
                + "amount = ?, product_status = ? WHERE id = ?");
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setString(2, product.getTrademark());
        preparedStatement.setDouble(3, product.getPrice());
        preparedStatement.setString(4, product.getCategory().name());
        preparedStatement.setString(5, product.getSize());
        preparedStatement.setLong(6, product.getAmount());
        preparedStatement.setString(7, product.getProductStatus().name());
        preparedStatement.setInt(8, product.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
}
