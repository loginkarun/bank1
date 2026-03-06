package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard error response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private String traceId;
    private String errorCode;
    private String message;
    
    @Builder.Default
    private List<ErrorDetail> details = new ArrayList<>();
}
