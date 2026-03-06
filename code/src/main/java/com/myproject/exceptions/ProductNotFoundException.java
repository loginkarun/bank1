package com.myproject.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a product is not found
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(UUID productId) {
        super("Product not found with ID: " + productId);
    }
    
    public ProductNotFoundException(String message) {
        super(message);
    }
}
