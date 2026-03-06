package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.AddItemRequest;
import com.myproject.models.dtos.CartDTO;
import com.myproject.models.dtos.RemoveItemRequest;
import com.myproject.models.dtos.UpdateItemRequest;
import com.myproject.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CartService cartService;
    
    private UUID userId;
    private UUID productId;
    private CartDTO cartDTO;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        
        cartDTO = CartDTO.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .items(new ArrayList<>())
            .total(BigDecimal.ZERO)
            .build();
    }
    
    @Test
    @WithMockUser(username = "admin")
    void addItem_Success() throws Exception {
        // Arrange
        AddItemRequest request = new AddItemRequest(productId, 2);
        when(cartService.addItemToCart(any(UUID.class), any(AddItemRequest.class)))
            .thenReturn(cartDTO);
        
        // Act & Assert
        mockMvc.perform(post("/api/cart/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    @WithMockUser(username = "admin")
    void getCart_Success() throws Exception {
        // Arrange
        when(cartService.getCart(any(UUID.class))).thenReturn(cartDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/cart")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    @WithMockUser(username = "admin")
    void removeItem_Success() throws Exception {
        // Arrange
        RemoveItemRequest request = new RemoveItemRequest(productId);
        when(cartService.removeItemFromCart(any(UUID.class), eq(productId)))
            .thenReturn(cartDTO);
        
        // Act & Assert
        mockMvc.perform(delete("/api/cart/remove")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    @WithMockUser(username = "admin")
    void updateItem_Success() throws Exception {
        // Arrange
        UpdateItemRequest request = new UpdateItemRequest(productId, 5);
        when(cartService.updateItemQuantity(any(UUID.class), any(UpdateItemRequest.class)))
            .thenReturn(cartDTO);
        
        // Act & Assert
        mockMvc.perform(put("/api/cart/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    @WithMockUser(username = "admin")
    void clearCart_Success() throws Exception {
        // Arrange
        when(cartService.clearCart(any(UUID.class))).thenReturn(cartDTO);
        
        // Act & Assert
        mockMvc.perform(delete("/api/cart/clear")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    void addItem_Unauthorized() throws Exception {
        // Arrange
        AddItemRequest request = new AddItemRequest(productId, 2);
        
        // Act & Assert
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}
