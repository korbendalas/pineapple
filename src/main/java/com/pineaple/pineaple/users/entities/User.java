package com.pineaple.pineaple.users.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static User create(UUID id, String email, String hashedPassword, LocalDateTime createdAt) {
        if (id == null) throw new IllegalArgumentException("User id must not be null");
        if(email == null || email.isBlank()) throw new IllegalArgumentException("Email must not be blank");
        if(hashedPassword == null || hashedPassword.isBlank()) throw new IllegalArgumentException("Password must not be blank");
        if(createdAt == null) throw new IllegalArgumentException("CreatedAt must not be null");
        User user = new User();
        user.id = id;
        user.email= email.toLowerCase(Locale.ROOT).strip();
        user.password = hashedPassword;
        user.createdAt = createdAt;
        return user;
    }
}

