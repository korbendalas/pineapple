package com.pineaple.pineaple.auth.dto;

public record AuthResult(String accessToken, String refreshToken, long expiresIn) {}

