package com.myproject.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for removing an item from the cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveItemRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;
}