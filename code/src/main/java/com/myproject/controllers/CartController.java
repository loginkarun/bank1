package com.myproject.controllers;

import com.myproject.models.dtos.*;
import com.myproject.services.interfaces.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Cart Management operations
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart Management", description = "Operations related to shopping cart management")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @Operation(
        summary = "Add item to cart",
        description = "Adds a product to the shopping cart with specified quantity",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CartDTO> addItem(
            @Valid @RequestBody AddItemRequest request,
            @RequestHeader(value = "X-User-ID", required = false) UUID userId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        
        log.info("POST /api/cart/add - Adding item to cart");
        CartDTO cart = cartService.addItemToCart(request, userId, sessionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping
    @Operation(
        summary = "Get cart",
        description = "Retrieves the current shopping cart for the authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CartDTO> getCart(
            @RequestHeader(value = "X-User-ID", required = false) UUID userId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        
        log.info("GET /api/cart - Retrieving cart");
        CartDTO cart = cartService.getCart(userId, sessionId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    @Operation(
        summary = "Remove item from cart",
        description = "Removes a specific product from the shopping cart",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CartDTO> removeItem(
            @Valid @RequestBody RemoveItemRequest request,
            @RequestHeader(value = "X-User-ID", required = false) UUID userId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        
        log.info("DELETE /api/cart/remove - Removing item from cart");
        CartDTO cart = cartService.removeItemFromCart(request, userId, sessionId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    @Operation(
        summary = "Update item quantity",
        description = "Updates the quantity of a specific product in the shopping cart",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CartDTO> updateItem(
            @Valid @RequestBody UpdateItemRequest request,
            @RequestHeader(value = "X-User-ID", required = false) UUID userId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        
        log.info("PUT /api/cart/update - Updating item quantity");
        CartDTO cart = cartService.updateItemQuantity(request, userId, sessionId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    @Operation(
        summary = "Clear cart",
        description = "Removes all items from the shopping cart",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CartDTO> clearCart(
            @RequestHeader(value = "X-User-ID", required = false) UUID userId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        
        log.info("DELETE /api/cart/clear - Clearing cart");
        CartDTO cart = cartService.clearCart(userId, sessionId);
        return ResponseEntity.ok(cart);
    }
}