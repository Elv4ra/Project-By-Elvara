package com.ProjectByElvara.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private final Map<Integer, ProductDTO> cartProductList;

    public Cart() {
        this.cartProductList = new HashMap<>();
    }

    public ProductDTO hasProduct(Integer id) {
        return this.cartProductList.get(id);
    }

    public void addProduct(ProductDTO product) {
        this.cartProductList.put(product.getId(), product);
    }

    public ProductDTO addProduct(Integer id, Long amount) {
        ProductDTO product = this.cartProductList.get(id);
        product.setAmount(product.getAmount() + amount);
        return this.cartProductList.put(product.getId(), product);
    }

    public ProductDTO removeFromCart(Integer id) {
        return this.cartProductList.remove(id);
    }

    public List<ProductDTO> getProducts() {
        return new ArrayList<>(this.cartProductList.values());
    }

    public void removeAll() {
        this.cartProductList.clear();
    }

    public Double getTotal() {
        return this.cartProductList.values().stream().mapToDouble(prod -> prod.getAmount() * prod.getPrice()).sum();
    }
}