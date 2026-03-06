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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
        
        cartDTO = new CartDTO();
        cartDTO.setId(UUID.randomUUID());
        cartDTO.setUserId(userId);
        cartDTO.setItems(new ArrayList<>());
        cartDTO.setTotal(BigDecimal.ZERO);
    }

    @Test
    void testAddItem_Success() throws Exception {
        AddItemRequest request = new AddItemRequest(productId, 2);
        
        when(cartService.addItemToCart(any(AddItemRequest.class), eq(userId), any()))
            .thenReturn(cartDTO);
        
        mockMvc.perform(post("/api/cart/add")
                .header("X-User-ID", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetCart_Success() throws Exception {
        when(cartService.getCart(eq(userId), any())).thenReturn(cartDTO);
        
        mockMvc.perform(get("/api/cart")
                .header("X-User-ID", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testRemoveItem_Success() throws Exception {
        RemoveItemRequest request = new RemoveItemRequest(productId);
        
        when(cartService.removeItemFromCart(any(RemoveItemRequest.class), eq(userId), any()))
            .thenReturn(cartDTO);
        
        mockMvc.perform(delete("/api/cart/remove")
                .header("X-User-ID", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateItem_Success() throws Exception {
        UpdateItemRequest request = new UpdateItemRequest(productId, 5);
        
        when(cartService.updateItemQuantity(any(UpdateItemRequest.class), eq(userId), any()))
            .thenReturn(cartDTO);
        
        mockMvc.perform(put("/api/cart/update")
                .header("X-User-ID", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void testClearCart_Success() throws Exception {
        when(cartService.clearCart(eq(userId), any())).thenReturn(cartDTO);
        
        mockMvc.perform(delete("/api/cart/clear")
                .header("X-User-ID", userId.toString()))
            .andExpect(status().isOk());
    }
}