package com.myproject.exceptions;

import com.myproject.models.dtos.ErrorDetail;
import com.myproject.models.dtos.ErrorResponse;
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
 * Global exception handler for REST controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ProductNotFoundException
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .traceId(UUID.randomUUID().toString())
            .errorCode("PRODUCT_NOT_FOUND")
            .message(ex.getMessage())
            .details(new ArrayList<>())
            .build();
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle OutOfStockException
     */
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStockException(
            OutOfStockException ex, WebRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .traceId(UUID.randomUUID().toString())
            .errorCode("OUT_OF_STOCK")
            .message(ex.getMessage())
            .details(new ArrayList<>())
            .build();
        
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    /**
     * Handle CartNotFoundException
     */
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(
            CartNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .traceId(UUID.randomUUID().toString())
            .errorCode("CART_NOT_FOUND")
            .message(ex.getMessage())
            .details(new ArrayList<>())
            .build();
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(ErrorDetail.builder()
                .field(error.getField())
                .issue(error.getDefaultMessage())
                .build());
        }
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .traceId(UUID.randomUUID().toString())
            .errorCode("VALIDATION_ERROR")
            .message("Validation failed")
            .details(details)
            .build();
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .traceId(UUID.randomUUID().toString())
            .errorCode("INTERNAL_ERROR")
            .message("An unexpected error occurred")
            .details(new ArrayList<>())
            .build();
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
