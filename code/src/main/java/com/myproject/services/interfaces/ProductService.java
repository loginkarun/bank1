package com.myproject.services.interfaces;

import com.myproject.models.entities.Product;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for product operations
 */
public interface ProductService {

    /**
     * Find product by ID
     */
    Product findById(UUID productId);

    /**
     * Check if product exists
     */
    boolean existsById(UUID productId);

    /**
     * Validate product stock
     */
    boolean hasStock(UUID productId, int quantity);

    /**
     * Get all products
     */
    List<Product> getAllProducts();

    /**
     * Create a new product
     */
    Product createProduct(Product product);
}