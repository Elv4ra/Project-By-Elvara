package com.ProjectByElvara.services;

import com.ProjectByElvara.dao.interfaces.IOrderDAO;
import com.ProjectByElvara.dao.interfaces.IProductDAO;
import com.ProjectByElvara.dao.interfaces.IUserDAO;
import com.ProjectByElvara.dto.UserDTO;
import com.ProjectByElvara.entities.Order;
import com.ProjectByElvara.entities.OrderProduct;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.User;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;
import com.ProjectByElvara.mapper.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final IUserDAO userDAO;
    private final IProductDAO productDAO;
    private final IOrderDAO orderDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserDAO userDAO, IProductDAO productDAO, IOrderDAO orderDAO,
                       PasswordEncoder encoder) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.passwordEncoder = encoder;
    }

    public boolean signUpUser(UserDTO newUser) throws SQLException {
        UserDTO maybeRegisteredUser = this.getUserByEmail(newUser.getEmail());
        if (maybeRegisteredUser != null) {
            return false;
        }
        newUser.setUserPassword(this.passwordEncoder.encode(newUser.getUserPassword()));
        this.userDAO.save(Mapper.convertToEntity(newUser));
        return true;
    }

    public UserDTO getUserById(Integer id) throws SQLException {
        Optional<User> maybeUser = this.userDAO.findById(id);
        if (maybeUser.isEmpty()) {
            return null;
        }
        return Mapper.convertToDTO(maybeUser.get());
     }

    private UserDTO getUserByEmail(String email) throws SQLException {
        Optional<User> maybeUser = this.userDAO.findByEmail(email);
        if (maybeUser.isEmpty()) {
            return null;
        }
        return Mapper.convertToDTO(maybeUser.get());
    }

    public List<UserDTO> getAllUsers() throws SQLException {
        return this.userDAO.findAll().stream().map(Mapper::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsers(String emailPattern) throws SQLException {
        return this.userDAO.findAllByEmailPattern(emailPattern).stream().map(Mapper::convertToDTO).collect(Collectors.toList());
    }

    public void updateUserInfo(UserDTO updatedUser) throws SQLException {
        this.userDAO.update(Mapper.convertToEntity(updatedUser));
    }

    public boolean updateUserPassword(String id, String oldPassword, String newPassword) throws SQLException {
        User user = this.userDAO.findById(Integer.parseInt(id)).orElseThrow(SQLException::new);
        if (!this.passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            return false;
        }
        user.setUserPassword(this.passwordEncoder.encode(newPassword));
        this.userDAO.update(user);
        return true;
    }

    public void deleteUser(String userEmail) throws SQLException {
        User user = this.userDAO.findByEmail(userEmail).orElseThrow(SQLException::new);
        for (Order order : this.orderDAO.findAllByUserId(user.getId())) {
            if (order.getOrderStatus() == OrderStatus.ACCEPTED) {
                for (OrderProduct orderProduct : order.getOrderProducts()) {
                    Product product = this.productDAO.findById(orderProduct.getProduct().getId()).orElseThrow(SQLException::new);
                    product.setAmount(product.getAmount() + orderProduct.getAmount());
                    this.productDAO.update(product);
                }
            }
            this.orderDAO.delete(order);
        }
        this.userDAO.delete(user);
    }
}
