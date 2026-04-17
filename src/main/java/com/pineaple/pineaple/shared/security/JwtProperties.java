package com.pineaple.pineaple.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds app.jwt.* properties from application.yaml.
 * Override APP_JWT_SECRET in production via environment variable.
 */
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpiry,
        long refreshTokenExpiry
) {}
