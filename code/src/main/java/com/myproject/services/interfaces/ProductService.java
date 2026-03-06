package com.myproject.services.interfaces;

import com.myproject.models.entities.Product;

import java.util.UUID;

/**
 * Service interface for product operations
 */
public interface ProductService {
    
    /**
     * Get product by ID
     */
    Product getProductById(UUID productId);
    
    /**
     * Validate product stock
     */
    void validateStock(UUID productId, int quantity);
}
