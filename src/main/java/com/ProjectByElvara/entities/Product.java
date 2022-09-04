package com.ProjectByElvara.entities;


import com.ProjectByElvara.entities.additionalClasses.Category;
import com.ProjectByElvara.entities.additionalClasses.ProductStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.DecimalFormat;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_name")
    private String productName;
    private String trademark;
    private Double price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
    private String size;
    private Long amount;
    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public Product() {
    }

    public Product(Integer id, String productName, String trademark, Double price, Category category, String size, Long amount, ProductStatus productStatus) {
        this.id = id;
        this.productName = productName;
        this.trademark = trademark;
        this.price = price;
        this.category = category;
        this.size = size;
        this.amount = amount;
        this.productStatus = productStatus == null ? ProductStatus.ACTIVE : productStatus;
    }

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getTrademark() {
        return trademark;
    }

    public Double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getSize() {
        return size;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
