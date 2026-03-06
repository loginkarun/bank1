package com.myproject.exceptions;

import com.myproject.models.dtos.ErrorDetail;
import com.myproject.models.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for all REST controllers
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        
        log.error("Product not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "PRODUCT_NOT_FOUND",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStockException(
            OutOfStockException ex, WebRequest request) {
        
        log.error("Out of stock: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "OUT_OF_STOCK",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(
            CartNotFoundException ex, WebRequest request) {
        
        log.error("Cart not found: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "CART_NOT_FOUND",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantityException(
            InvalidQuantityException ex, WebRequest request) {
        
        log.error("Invalid quantity: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "INVALID_QUANTITY",
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        log.error("Validation error: {}", ex.getMessage());
        
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
        }
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "VALIDATION_ERROR",
            "Input validation failed"
        );
        error.setDetails(details);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            UUID.randomUUID().toString(),
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later."
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}