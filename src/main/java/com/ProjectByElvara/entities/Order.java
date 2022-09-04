package com.ProjectByElvara.entities;

import com.ProjectByElvara.entities.additionalClasses.Delivery;
import com.ProjectByElvara.entities.additionalClasses.Payment;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderProduct> orderProducts = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Payment payment;
    @Enumerated(EnumType.STRING)
    private Delivery delivery;
    private String address;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "update_time")
    private Timestamp updateTime;

    public Order() {}

    public Order(Integer id, User user, Payment payment, Delivery delivery, String address, OrderStatus status, Timestamp updateTime) {
        this.id = id;
        this.user = user;
        this.payment = payment;
        this.delivery = delivery;
        this.address = address;
        this.orderStatus = status == null ? OrderStatus.ACCEPTED : status;
        this.updateTime = updateTime == null ? new Timestamp(Instant.now().toEpochMilli()) : updateTime;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public Payment getPayment() {
        return payment;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
