package com.myproject.services.impl;

import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    private UUID productId;
    private Product product;
    
    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = Product.builder()
            .id(productId)
            .name("Test Product")
            .price(new BigDecimal("29.99"))
            .stock(100)
            .build();
    }
    
    @Test
    void getProductById_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        Product result = productService.getProductById(productId);
        
        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
    }
    
    @Test
    void getProductById_NotFound_ThrowsException() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ProductNotFoundException.class, 
            () -> productService.getProductById(productId));
    }
    
    @Test
    void validateStock_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act & Assert
        assertDoesNotThrow(() -> productService.validateStock(productId, 50));
    }
    
    @Test
    void validateStock_InsufficientStock_ThrowsException() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act & Assert
        assertThrows(OutOfStockException.class, 
            () -> productService.validateStock(productId, 200));
    }
}
