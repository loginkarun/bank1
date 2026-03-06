package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    
    private UUID id;
    private UUID userId;
    private String sessionId;
    
    @Builder.Default
    private List<CartItemDTO> items = new ArrayList<>();
    
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
