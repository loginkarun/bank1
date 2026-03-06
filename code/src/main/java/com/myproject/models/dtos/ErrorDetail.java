package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detailed error information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    private String field;
    private String issue;
}