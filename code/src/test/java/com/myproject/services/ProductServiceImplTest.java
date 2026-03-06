package com.myproject.services;

import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("29.99"));
        product.setStock(100);
    }

    @Test
    void testFindById_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        Product result = productService.findById(productId);
        
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(productId);
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        assertThrows(ProductNotFoundException.class, () -> {
            productService.findById(productId);
        });
    }

    @Test
    void testExistsById_True() {
        when(productRepository.existsById(productId)).thenReturn(true);
        
        boolean result = productService.existsById(productId);
        
        assertTrue(result);
        verify(productRepository).existsById(productId);
    }

    @Test
    void testExistsById_False() {
        when(productRepository.existsById(productId)).thenReturn(false);
        
        boolean result = productService.existsById(productId);
        
        assertFalse(result);
    }

    @Test
    void testHasStock_True() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        boolean result = productService.hasStock(productId, 50);
        
        assertTrue(result);
    }

    @Test
    void testHasStock_False() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        boolean result = productService.hasStock(productId, 150);
        
        assertFalse(result);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);
        
        List<Product> result = productService.getAllProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        Product result = productService.createProduct(product);
        
        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository).save(any(Product.class));
    }
}