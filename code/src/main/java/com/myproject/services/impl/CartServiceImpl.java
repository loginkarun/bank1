package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.InvalidQuantityException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.*;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.interfaces.CartService;
import com.myproject.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of CartService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Override
    public CartDTO addItemToCart(AddItemRequest request, UUID userId, String sessionId) {
        log.info("Adding item to cart - ProductID: {}, Quantity: {}", request.getProductId(), request.getQuantity());
        
        // Validate product exists
        Product product = productService.findById(request.getProductId());
        
        // Validate stock
        if (!product.hasStock(request.getQuantity())) {
            throw new OutOfStockException(request.getProductId(), request.getQuantity(), product.getStock());
        }
        
        // Get or create cart
        Cart cart = getOrCreateCart(userId, sessionId);
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (!product.hasStock(newQuantity)) {
                throw new OutOfStockException(request.getProductId(), newQuantity, product.getStock());
            }
            
            item.setQuantity(newQuantity);
            item.calculateSubtotal();
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            cart.addItem(newItem);
        }
        
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        log.info("Item added successfully to cart ID: {}", savedCart.getId());
        return convertToDTO(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO getCart(UUID userId, String sessionId) {
        log.debug("Fetching cart for userId: {} or sessionId: {}", userId, sessionId);
        Cart cart = findCart(userId, sessionId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found", true));
        return convertToDTO(cart);
    }

    @Override
    public CartDTO removeItemFromCart(RemoveItemRequest request, UUID userId, String sessionId) {
        log.info("Removing item from cart - ProductID: {}", request.getProductId());
        
        Cart cart = findCart(userId, sessionId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found", true));
        
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));
        
        cart.removeItem(itemToRemove);
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        log.info("Item removed successfully from cart ID: {}", savedCart.getId());
        return convertToDTO(savedCart);
    }

    @Override
    public CartDTO updateItemQuantity(UpdateItemRequest request, UUID userId, String sessionId) {
        log.info("Updating item quantity - ProductID: {}, NewQuantity: {}", request.getProductId(), request.getQuantity());
        
        if (request.getQuantity() <= 0) {
            throw new InvalidQuantityException(request.getQuantity());
        }
        
        // Validate product and stock
        Product product = productService.findById(request.getProductId());
        if (!product.hasStock(request.getQuantity())) {
            throw new OutOfStockException(request.getProductId(), request.getQuantity(), product.getStock());
        }
        
        Cart cart = findCart(userId, sessionId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found", true));
        
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));
        
        item.setQuantity(request.getQuantity());
        item.calculateSubtotal();
        cart.recalculateTotal();
        
        Cart savedCart = cartRepository.save(cart);
        
        log.info("Item quantity updated successfully in cart ID: {}", savedCart.getId());
        return convertToDTO(savedCart);
    }

    @Override
    public CartDTO clearCart(UUID userId, String sessionId) {
        log.info("Clearing cart for userId: {} or sessionId: {}", userId, sessionId);
        
        Cart cart = findCart(userId, sessionId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found", true));
        
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);
        
        log.info("Cart cleared successfully - Cart ID: {}", savedCart.getId());
        return convertToDTO(savedCart);
    }

    /**
     * Find existing cart by userId or sessionId
     */
    private Optional<Cart> findCart(UUID userId, String sessionId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId);
        } else if (sessionId != null && !sessionId.isEmpty()) {
            return cartRepository.findBySessionId(sessionId);
        }
        return Optional.empty();
    }

    /**
     * Get existing cart or create new one
     */
    private Cart getOrCreateCart(UUID userId, String sessionId) {
        Optional<Cart> existingCart = findCart(userId, sessionId);
        
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setSessionId(sessionId);
        newCart.setTotal(BigDecimal.ZERO);
        return cartRepository.save(newCart);
    }

    /**
     * Convert Cart entity to CartDTO
     */
    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setSessionId(cart.getSessionId());
        dto.setTotal(cart.getTotal());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        
        dto.setItems(cart.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList()));
        
        return dto;
    }

    /**
     * Convert CartItem entity to CartItemDTO
     */
    private CartItemDTO convertItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}