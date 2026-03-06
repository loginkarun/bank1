package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO representing a shopping cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private UUID id;
    private UUID userId;
    private String sessionId;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}