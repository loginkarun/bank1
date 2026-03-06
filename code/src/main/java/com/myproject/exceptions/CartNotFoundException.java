package com.myproject.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a cart is not found
 */
public class CartNotFoundException extends RuntimeException {
    
    public CartNotFoundException(UUID userId) {
        super("Cart not found for user: " + userId);
    }
    
    public CartNotFoundException(String message) {
        super(message);
    }
}
