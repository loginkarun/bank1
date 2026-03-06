package com.myproject.services;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.RemoveItemRequest;
import com.myproject.models.dtos.UpdateItemRequest;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.impl.CartServiceImpl;
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
        
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("29.99"));
        product.setStock(100);
        
        cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.ZERO);
    }

    @Test
    void testAddItemToCart_Success() {
        AddItemRequest request = new AddItemRequest(productId, 2);
        
        when(productService.findById(productId)).thenReturn(product);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        CartDTO result = cartService.addItemToCart(request, userId, null);
        
        assertNotNull(result);
        verify(productService).findById(productId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_ProductNotFound() {
        AddItemRequest request = new AddItemRequest(productId, 2);
        
        when(productService.findById(productId)).thenThrow(new ProductNotFoundException(productId));
        
        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addItemToCart(request, userId, null);
        });
    }

    @Test
    void testAddItemToCart_OutOfStock() {
        AddItemRequest request = new AddItemRequest(productId, 200);
        product.setStock(50);
        
        when(productService.findById(productId)).thenReturn(product);
        
        assertThrows(OutOfStockException.class, () -> {
            cartService.addItemToCart(request, userId, null);
        });
    }

    @Test
    void testGetCart_Success() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        CartDTO result = cartService.getCart(userId, null);
        
        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void testGetCart_NotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId, null);
        });
    }

    @Test
    void testRemoveItemFromCart_Success() {
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProductId(productId);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("29.99"));
        item.setCart(cart);
        cart.getItems().add(item);
        
        RemoveItemRequest request = new RemoveItemRequest(productId);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        CartDTO result = cartService.removeItemFromCart(request, userId, null);
        
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_Success() {
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProductId(productId);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("29.99"));
        item.setCart(cart);
        cart.getItems().add(item);
        
        UpdateItemRequest request = new UpdateItemRequest(productId, 5);
        
        when(productService.findById(productId)).thenReturn(product);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        CartDTO result = cartService.updateItemQuantity(request, userId, null);
        
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testClearCart_Success() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        
        CartDTO result = cartService.clearCart(userId, null);
        
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotal());
        assertTrue(result.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }
}