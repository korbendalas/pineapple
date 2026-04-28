package com.pineaple.pineaple.auth.services;

import com.pineaple.pineaple.auth.dto.AuthResult;
import com.pineaple.pineaple.auth.entities.RefreshToken;
import com.pineaple.pineaple.auth.exceptions.InvalidCredentialsException;
import com.pineaple.pineaple.auth.repositories.RefreshTokenRepository;
import com.pineaple.pineaple.users.entities.User;
import com.pineaple.pineaple.users.exceptions.UserAlreadyExistsException;
import com.pineaple.pineaple.users.repositories.UserRepository;
import com.pineaple.pineaple.shared.security.JwtProperties;
import com.pineaple.pineaple.shared.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles registration, login, token refresh, and logout.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional
    public AuthResult login(String email, String password) {
        String normalizedEmail = email.toLowerCase().strip();
        log.info("Logging in user with email={}", normalizedEmail);
        User user = userRepository.findByEmail(normalizedEmail).orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        log.info("User logged in. userId={}", user.getId());
        return issueTokenPair(user);
    }

    @Transactional
    public AuthResult register(String email, String password) {
        String normalized = email.toLowerCase().strip();
        if (userRepository.existsByEmail(normalized)) {
            throw new UserAlreadyExistsException(normalized);
        }
        User user = User.create(normalized, passwordEncoder.encode(password));
        log.info("Creating user. userId={}, email={}", user.getUserId(), user.getEmail());
        userRepository.save(user);
        log.info("User registered. userId={}", user.getUserId());
        return issueTokenPair(user);

    }

    private AuthResult issueTokenPair(User user) {
        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getEmail());
        String rawRefreshToken = jwtService.generateRefreshToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtProperties.refreshTokenExpiry() / 1000);
        refreshTokenRepository.save(RefreshToken.create(user.getId(), rawRefreshToken, expiresAt));
        return new AuthResult(accessToken, rawRefreshToken, jwtProperties.accessTokenExpiry());
    }
}
