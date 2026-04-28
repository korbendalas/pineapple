package com.pineaple.pineaple.products.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @Size(min = 1, max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price,

        @Size(min = 3, max = 3)
        String currency
) {
}