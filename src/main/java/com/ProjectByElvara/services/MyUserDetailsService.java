package com.ProjectByElvara.services;

import com.ProjectByElvara.dao.interfaces.IUserDAO;
import com.ProjectByElvara.entities.User;
import com.ProjectByElvara.entities.additionalClasses.UserRole;
import com.ProjectByElvara.security.MyUserDetails;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    private final IUserDAO userDAO;

    public MyUserDetailsService(IUserDAO userDAO, PasswordEncoder passwordEncoder) throws SQLException {
        this.userDAO = userDAO;
        Optional<User> adminAccount = this.userDAO.findByEmail(System.getenv("ADMIN_ACCOUNT_EMAIL"));
        if (adminAccount.isEmpty()) {
            this.userDAO.save(new User(null,
                    System.getenv("ADMIN_ACCOUNT_EMAIL"),
                    passwordEncoder.encode(System.getenv("ADMIN_ACCOUNT_PASSWORD")),
                    System.getenv("ADMIN_ACCOUNT_NAME"),
                    System.getenv("ADMIN_ACCOUNT_SURNAME"),
                    System.getenv("ADMIN_ACCOUNT_PHONE"),
                    UserRole.ROLE_ADMIN));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.userDAO.findByEmail(username).orElseThrow(SQLException::new);
            return new MyUserDetails(user);
        } catch (SQLException e) {
            throw new UsernameNotFoundException("No Account With This Email");
        }
    }

    public boolean hasAccess(UserRole userRole) {
        return ((MyUserDetails) this.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        )).hasAuthority(userRole);
    }

    public Integer getSessionUserId() {
        return ((MyUserDetails) this.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        )).getUser().getId();
    }
}
