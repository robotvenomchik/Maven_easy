package org.example.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalCost;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        calculateTotalCost();
    }

    public void calculateTotalCost() {
        if (products != null) {
            totalCost = products.stream().mapToDouble(Product::getPrice).sum();
        } else {
            totalCost = 0.0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        calculateTotalCost();
    }

    public Order(Long id, List<Product> products, LocalDateTime createdAt) {
        this.id = id;
        this.products = products;
        this.createdAt = createdAt;
        calculateTotalCost();
    }

    public Order(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        calculateTotalCost();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
