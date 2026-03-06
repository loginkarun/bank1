package com.myproject.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a cart is not found
 */
public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(UUID userId) {
        super("Cart not found for user ID: " + userId);
    }

    public CartNotFoundException(String sessionId) {
        super("Cart not found for session ID: " + sessionId);
    }

    public CartNotFoundException(String message, boolean isCustom) {
        super(message);
    }
}