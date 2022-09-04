package com.ProjectByElvara.dao.interfaces;

import com.ProjectByElvara.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    Optional<User> findById(Integer id) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    List<User> findAll() throws SQLException;

    List<User> findAllByEmailPattern(String emailPattern) throws SQLException;

    void save(User user) throws SQLException;

    void update(User user) throws SQLException;

    void delete(User user) throws SQLException;
//
//    void initialSave(User user) throws SQLException;
}
