package com.myproject.services.interfaces;

import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.RemoveItemRequest;
import com.myproject.models.dtos.UpdateItemRequest;

import java.util.UUID;

/**
 * Service interface for cart operations
 */
public interface CartService {

    /**
     * Add an item to the cart
     */
    CartDTO addItemToCart(AddItemRequest request, UUID userId, String sessionId);

    /**
     * Get cart for user or session
     */
    CartDTO getCart(UUID userId, String sessionId);

    /**
     * Remove an item from the cart
     */
    CartDTO removeItemFromCart(RemoveItemRequest request, UUID userId, String sessionId);

    /**
     * Update item quantity in the cart
     */
    CartDTO updateItemQuantity(UpdateItemRequest request, UUID userId, String sessionId);

    /**
     * Clear all items from the cart
     */
    CartDTO clearCart(UUID userId, String sessionId);
}