package com.myproject.services.interfaces;

import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.UpdateItemRequest;

import java.util.UUID;

/**
 * Service interface for cart operations
 */
public interface CartService {
    
    /**
     * Add item to cart
     */
    CartDTO addItemToCart(UUID userId, AddItemRequest request);
    
    /**
     * Get cart for user
     */
    CartDTO getCart(UUID userId);
    
    /**
     * Remove item from cart
     */
    CartDTO removeItemFromCart(UUID userId, UUID productId);
    
    /**
     * Update item quantity in cart
     */
    CartDTO updateItemQuantity(UUID userId, UpdateItemRequest request);
    
    /**
     * Clear cart
     */
    CartDTO clearCart(UUID userId);
}
