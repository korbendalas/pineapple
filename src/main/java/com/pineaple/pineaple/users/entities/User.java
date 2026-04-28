package com.pineaple.pineaple.users.entities;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static User create(String email, String hashedPassword) {
        if(email == null || email.isBlank()) throw new IllegalArgumentException("Email must not be blank");
        if(hashedPassword == null || hashedPassword.isBlank()) throw new IllegalArgumentException("Password must not be blank");
        User user = new User();
        user.userId = UUID.randomUUID();
        user.email= email.toLowerCase(Locale.ROOT).strip();
        user.password = hashedPassword;
        user.createdAt = LocalDateTime.now();
        return user;
    }
}
