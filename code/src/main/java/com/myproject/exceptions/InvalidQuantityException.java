package com.myproject.exceptions;

/**
 * Exception thrown when an invalid quantity is provided
 */
public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(int quantity) {
        super("Invalid quantity: " + quantity + ". Quantity must be greater than 0.");
    }

    public InvalidQuantityException(String message) {
        super(message);
    }
}