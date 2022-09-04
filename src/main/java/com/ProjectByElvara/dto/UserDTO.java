package com.ProjectByElvara.dto;

import com.ProjectByElvara.entities.additionalClasses.UserRole;

public class UserDTO {
    private Integer id;
    private String email;
    private String userPassword;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole userRole;

    public UserDTO() {
    }

    public UserDTO(Integer id, String email, String userPassword, String firstName, String lastName, String phone, UserRole userType) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
