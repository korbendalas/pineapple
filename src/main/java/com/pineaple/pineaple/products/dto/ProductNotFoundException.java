package com.pineaple.pineaple.products.dto;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(UUID productId) {
        super("Product with id '%s' was not found.".formatted(productId));
    }
}
