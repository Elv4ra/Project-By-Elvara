package com.ProjectByElvara.dto;

import com.ProjectByElvara.entities.additionalClasses.Category;
import com.ProjectByElvara.entities.additionalClasses.ProductStatus;

public class ProductDTO {
    private Integer id;
    private String productName;
    private String trademark;
    private Double price;
    private Category category;
    private String size;
    private Long amount;
    private ProductStatus status;

    public ProductDTO() {
    }

    public ProductDTO(Integer id, String productName, String trademark, Double price, Category category, String size, Long amount, ProductStatus status) {
        this.id = id;
        this.productName = productName;
        this.trademark = trademark;
        this.price = price;
        this.category = category;
        this.size = size;
        this.amount = amount;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
