package com.myproject.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entity representing a product
 */
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    /**
     * Check if product has sufficient stock
     */
    public boolean hasStock(int quantity) {
        return this.stock >= quantity;
    }

    /**
     * Reduce stock by given quantity
     */
    public void reduceStock(int quantity) {
        if (hasStock(quantity)) {
            this.stock -= quantity;
        } else {
            throw new IllegalStateException("Insufficient stock for product: " + this.name);
        }
    }

    /**
     * Increase stock by given quantity
     */
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}