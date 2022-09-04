package com.ProjectByElvara.dao.jdbcImplementation;

import com.ProjectByElvara.dao.interfaces.IUserDAO;
import com.ProjectByElvara.entities.User;
import com.ProjectByElvara.entities.additionalClasses.UserRole;
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
public class UserDAO implements IUserDAO {
    private final DriverManagerDataSource dataSource;

    public UserDAO(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

//    @Override
//    public void initialSave(User user) throws SQLException {
//        Connection connection = this.dataSource.getConnection();
//        connection.setAutoCommit(false);
//        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "
//                + "users(email, user_password, first_name, last_name, phone, user_role) "
//                + "VALUE(?,?,?,?,?,?)");
//        preparedStatement.setString(1, user.getEmail());
//        preparedStatement.setString(2, user.getUserPassword());
//        preparedStatement.setString(3, user.getFirstName());
//        preparedStatement.setString(4, user.getLastName());
//        preparedStatement.setString(5, user.getPhone());
//        preparedStatement.setString(6, user.getUserRole().name());
//        preparedStatement.execute();
//        connection.commit();
//        preparedStatement.close();
//    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users;");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user = this.createUserEntity(resultSet);
            users.add(user);
        }
        resultSet.close();
        preparedStatement.close();
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users "
                + "WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = this.createUserEntity(resultSet);
        }
        resultSet.close();
        preparedStatement.close();
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users "
                + "WHERE email=?;");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = this.createUserEntity(resultSet);
        }
        resultSet.close();
        preparedStatement.close();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAllByEmailPattern(String emailPattern) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from users "
                + "WHERE email LIKE ?;");
        preparedStatement.setString(1, "%" + emailPattern + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user = this.createUserEntity(resultSet);
            users.add(user);
        }
        resultSet.close();
        preparedStatement.close();
        return users;
    }

    private User createUserEntity(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("user_password"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone"),
                UserRole.valueOfCode(resultSet.getString("user_role")));
    }

    @Override
    public void save(User user) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "
                + "users(email, user_password, first_name, last_name, phone, user_role) "
                + "VALUE(?,?,?,?,?,?)");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getUserPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getPhone());
        preparedStatement.setString(6, user.getUserRole().name());
        preparedStatement.execute();
        preparedStatement.close();
    }


    @Override
    public void update(User user) throws SQLException {
        System.out.println(user.getUserRole().name());
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users "
                + "SET email = ?, user_password = ?, first_name = ?, last_name = ?, phone = ?, "
                + "user_role = ? WHERE id = ?;");
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getUserPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getPhone());
        preparedStatement.setString(6, user.getUserRole().name());
        preparedStatement.setInt(7, user.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public void delete(User user) throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?;");
        preparedStatement.setInt(1, user.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
}
