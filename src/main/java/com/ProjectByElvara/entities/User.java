package com.ProjectByElvara.entities;

import com.ProjectByElvara.entities.additionalClasses.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, updatable = false)
    private String email;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String phone;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {
    }

    public User(Integer id, String email, String userPassword, String firstName, String lastName, String phone, UserRole userType) {
        this.id = id;
        this.email = email;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.userRole = userType;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
