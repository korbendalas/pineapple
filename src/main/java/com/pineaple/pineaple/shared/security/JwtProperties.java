package com.pineaple.pineaple.shared.security;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Binds app.jwt.* properties from application.yaml.
 * Override APP_JWT_SECRET in production via environment variable.
 */
@ConfigurationProperties(prefix = "app.jwt")
@Validated
public record JwtProperties(
        @NotBlank(message = "app.jwt.secret must not be blank")
        @Size(min = 32, message = "app.jwt.secret must be at least 32 characters long")
        String secret,
        @Min(value = 1, message = "app.jwt.access-token-expiry must be greater than 0")
        long accessTokenExpiry,
        @Min(value = 1, message = "app.jwt.refresh-token-expiry must be greater than 0")
        long refreshTokenExpiry
) {}
