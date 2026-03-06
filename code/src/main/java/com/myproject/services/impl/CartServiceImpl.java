package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.CartItemDTO;
import com.myproject.models.dtos.UpdateItemRequest;
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

import java.util.ArrayList;
import java.util.List;
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
    public CartDTO addItemToCart(UUID userId, AddItemRequest request) {
        log.info("Adding item to cart for user: {}, product: {}, quantity: {}", 
            userId, request.getProductId(), request.getQuantity());
        
        // Validate product and stock
        Product product = productService.getProductById(request.getProductId());
        productService.validateStock(request.getProductId(), request.getQuantity());
        
        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> createNewCart(userId));
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            productService.validateStock(request.getProductId(), newQuantity);
            item.setQuantity(newQuantity);
            item.calculateSubtotal();
            log.debug("Updated existing cart item quantity to: {}", newQuantity);
        } else {
            // Add new item
            CartItem newItem = CartItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .build();
            newItem.calculateSubtotal();
            cart.addItem(newItem);
            log.debug("Added new item to cart");
        }
        
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDTO(savedCart);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CartDTO getCart(UUID userId) {
        log.info("Fetching cart for user: {}", userId);
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException(userId));
        return convertToDTO(cart);
    }
    
    @Override
    public CartDTO removeItemFromCart(UUID userId, UUID productId) {
        log.info("Removing item from cart for user: {}, product: {}", userId, productId);
        
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException(userId));
        
        CartItem itemToRemove = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new CartNotFoundException("Product not found in cart"));
        
        cart.removeItem(itemToRemove);
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDTO(savedCart);
    }
    
    @Override
    public CartDTO updateItemQuantity(UUID userId, UpdateItemRequest request) {
        log.info("Updating item quantity in cart for user: {}, product: {}, new quantity: {}", 
            userId, request.getProductId(), request.getQuantity());
        
        // Validate product and stock
        productService.getProductById(request.getProductId());
        productService.validateStock(request.getProductId(), request.getQuantity());
        
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException(userId));
        
        CartItem item = cart.getItems().stream()
            .filter(cartItem -> cartItem.getProductId().equals(request.getProductId()))
            .findFirst()
            .orElseThrow(() -> new CartNotFoundException("Product not found in cart"));
        
        item.setQuantity(request.getQuantity());
        item.calculateSubtotal();
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDTO(savedCart);
    }
    
    @Override
    public CartDTO clearCart(UUID userId) {
        log.info("Clearing cart for user: {}", userId);
        
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException(userId));
        
        cart.getItems().clear();
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDTO(savedCart);
    }
    
    /**
     * Create a new cart for user
     */
    private Cart createNewCart(UUID userId) {
        log.debug("Creating new cart for user: {}", userId);
        return Cart.builder()
            .userId(userId)
            .items(new ArrayList<>())
            .build();
    }
    
    /**
     * Convert Cart entity to CartDTO
     */
    private CartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
            .map(this::convertItemToDTO)
            .collect(Collectors.toList());
        
        return CartDTO.builder()
            .id(cart.getId())
            .userId(cart.getUserId())
            .sessionId(cart.getSessionId())
            .items(itemDTOs)
            .total(cart.getTotal())
            .createdAt(cart.getCreatedAt())
            .updatedAt(cart.getUpdatedAt())
            .build();
    }
    
    /**
     * Convert CartItem entity to CartItemDTO
     */
    private CartItemDTO convertItemToDTO(CartItem item) {
        return CartItemDTO.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productName(item.getProductName())
            .quantity(item.getQuantity())
            .price(item.getPrice())
            .subtotal(item.getSubtotal())
            .build();
    }
}
