package com.ProjectByElvara.dao.interfaces;


import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.additionalClasses.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IProductDAO {
    Optional<Product> findById(Integer id) throws SQLException;

    Optional<Product> findByNameTrademarkAndSize(String productName, String trademark, String size) throws SQLException;

    List<Product> findAll() throws SQLException;

    List<Product> findAllByCategory(Category category) throws SQLException;

    void save(Product product) throws SQLException;

    void update(Product product) throws SQLException;
}
