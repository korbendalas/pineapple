package com.pineaple.pineaple.auth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "token", nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name="revoked", nullable = false)
    private boolean revoked;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public static RefreshToken create(Long userId, String token, LocalDateTime expiresAt) {
        if (userId == null) throw new IllegalArgumentException("User id must not be null");
        if(token == null || token.isBlank()) throw new IllegalArgumentException("Token must not be blank");
        if(expiresAt == null) throw new IllegalArgumentException("ExpiresAt must not be null");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.userId = userId;
        refreshToken.token = token;
        refreshToken.expiresAt = expiresAt;
        refreshToken.revoked = false;
        refreshToken.createdAt = LocalDateTime.now();
        return refreshToken;
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

}
