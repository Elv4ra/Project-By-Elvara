package com.ProjectByElvara.dto;

import com.ProjectByElvara.entities.additionalClasses.Delivery;
import com.ProjectByElvara.entities.additionalClasses.Payment;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;

import java.sql.Timestamp;
import java.util.List;

public class OrderDTO {
    private Integer id;
    private UserDTO user;
    private List<ProductDTO> products;
    private Payment payment;
    private Delivery delivery;
    private String address;
    private OrderStatus status;
    private Timestamp updateTime;

    public OrderDTO() {}

    public OrderDTO(Integer id, UserDTO user, List<ProductDTO> products, Payment payment, Delivery delivery, String address, OrderStatus status, Timestamp updateTime) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.payment = payment;
        this.delivery = delivery;
        this.address = address;
        this.status = status;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Double getTotal() {
        double total = 0d;
        for (ProductDTO orderProduct : this.products) {
            total += orderProduct.getPrice() * orderProduct.getAmount();
        }
        return total;
    }
}