package com.myproject.controllers;

import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.RemoveItemRequest;
import com.myproject.models.dtos.UpdateItemRequest;
import com.myproject.services.interfaces.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for shopping cart operations
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart Management", description = "Operations related to shopping cart management")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    
    private final CartService cartService;
    
    /**
     * Add item to cart
     */
    @PostMapping("/add")
    @Operation(summary = "Add item to cart", 
               description = "Adds a product to the shopping cart with specified quantity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully added to cart"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Out of stock")
    })
    public ResponseEntity<CartDTO> addItem(
            @Valid @RequestBody AddItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("POST /api/cart/add - Adding item to cart");
        UUID userId = extractUserId(userDetails);
        CartDTO cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
    
    /**
     * Get cart
     */
    @GetMapping
    @Operation(summary = "Get cart", 
               description = "Retrieves the current shopping cart for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<CartDTO> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("GET /api/cart - Fetching cart");
        UUID userId = extractUserId(userDetails);
        CartDTO cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Remove item from cart
     */
    @DeleteMapping("/remove")
    @Operation(summary = "Remove item from cart", 
               description = "Removes a specific product from the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item successfully removed from cart"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    public ResponseEntity<CartDTO> removeItem(
            @Valid @RequestBody RemoveItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("DELETE /api/cart/remove - Removing item from cart");
        UUID userId = extractUserId(userDetails);
        CartDTO cart = cartService.removeItemFromCart(userId, request.getProductId());
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Update item quantity
     */
    @PutMapping("/update")
    @Operation(summary = "Update item quantity", 
               description = "Updates the quantity of a specific product in the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item quantity successfully updated"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Out of stock")
    })
    public ResponseEntity<CartDTO> updateItem(
            @Valid @RequestBody UpdateItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("PUT /api/cart/update - Updating item quantity");
        UUID userId = extractUserId(userDetails);
        CartDTO cart = cartService.updateItemQuantity(userId, request);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Clear cart
     */
    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", 
               description = "Removes all items from the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart successfully cleared"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<CartDTO> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("DELETE /api/cart/clear - Clearing cart");
        UUID userId = extractUserId(userDetails);
        CartDTO cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Extract user ID from authenticated user details
     * For demo purposes, generates a random UUID
     * In production, this would extract from JWT or session
     */
    private UUID extractUserId(UserDetails userDetails) {
        // For demo: use a fixed UUID based on username
        // In production: extract from JWT token or session
        return UUID.nameUUIDFromBytes(userDetails.getUsername().getBytes());
    }
}
