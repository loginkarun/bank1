package com.myproject.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a product is out of stock
 */
public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(UUID productId, int requested, int available) {
        super(String.format("Product %s is out of stock. Requested: %d, Available: %d", 
            productId, requested, available));
    }
    
    public OutOfStockException(String message) {
        super(message);
    }
}
