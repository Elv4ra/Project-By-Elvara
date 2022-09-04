package com.ProjectByElvara.entities;

import javax.persistence.*;

@Entity
@Table(name = "order_products")
public class OrderProduct {
    @EmbeddedId
    private OrderProductPrimaryKey primaryKey;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "product_amount")
    private Long amount;

    public OrderProduct() {}

    public OrderProduct(Order order, Product product, Long amount) {
        this.primaryKey = new OrderProductPrimaryKey(order.getId(), product.getId());
        this.order = order;
        this.product = product;
        this.amount = amount;
    }

    public OrderProductPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(OrderProductPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
