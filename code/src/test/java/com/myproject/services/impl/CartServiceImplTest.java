package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.UpdateItemRequest;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    
    @Mock
    private CartRepository cartRepository;
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private CartServiceImpl cartService;
    
    private UUID userId;
    private UUID productId;
    private Product product;
    private Cart cart;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        
        product = Product.builder()
            .id(productId)
            .name("Test Product")
            .price(new BigDecimal("29.99"))
            .stock(100)
            .build();
        
        cart = Cart.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .items(new ArrayList<>())
            .total(BigDecimal.ZERO)
            .build();
    }
    
    @Test
    void addItemToCart_NewItem_Success() {
        // Arrange
        AddItemRequest request = new AddItemRequest(productId, 2);
        when(productService.getProductById(productId)).thenReturn(product);
        doNothing().when(productService).validateStock(productId, 2);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        // Act
        CartDTO result = cartService.addItemToCart(userId, request);
        
        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository).save(any(Cart.class));
    }
    
    @Test
    void addItemToCart_ProductNotFound_ThrowsException() {
        // Arrange
        AddItemRequest request = new AddItemRequest(productId, 2);
        when(productService.getProductById(productId))
            .thenThrow(new ProductNotFoundException(productId));
        
        // Act & Assert
        assertThrows(ProductNotFoundException.class, 
            () -> cartService.addItemToCart(userId, request));
    }
    
    @Test
    void addItemToCart_OutOfStock_ThrowsException() {
        // Arrange
        AddItemRequest request = new AddItemRequest(productId, 200);
        when(productService.getProductById(productId)).thenReturn(product);
        doThrow(new OutOfStockException(productId, 200, 100))
            .when(productService).validateStock(productId, 200);
        
        // Act & Assert
        assertThrows(OutOfStockException.class, 
            () -> cartService.addItemToCart(userId, request));
    }
    
    @Test
    void getCart_Success() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        CartDTO result = cartService.getCart(userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }
    
    @Test
    void getCart_NotFound_ThrowsException() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CartNotFoundException.class, 
            () -> cartService.getCart(userId));
    }
    
    @Test
    void removeItemFromCart_Success() {
        // Arrange
        CartItem item = CartItem.builder()
            .id(UUID.randomUUID())
            .productId(productId)
            .quantity(2)
            .price(new BigDecimal("29.99"))
            .build();
        cart.addItem(item);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        // Act
        CartDTO result = cartService.removeItemFromCart(userId, productId);
        
        // Assert
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }
    
    @Test
    void updateItemQuantity_Success() {
        // Arrange
        CartItem item = CartItem.builder()
            .id(UUID.randomUUID())
            .productId(productId)
            .quantity(2)
            .price(new BigDecimal("29.99"))
            .build();
        cart.addItem(item);
        
        UpdateItemRequest request = new UpdateItemRequest(productId, 5);
        when(productService.getProductById(productId)).thenReturn(product);
        doNothing().when(productService).validateStock(productId, 5);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        // Act
        CartDTO result = cartService.updateItemQuantity(userId, request);
        
        // Assert
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }
    
    @Test
    void clearCart_Success() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        // Act
        CartDTO result = cartService.clearCart(userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        verify(cartRepository).save(any(Cart.class));
    }
}
