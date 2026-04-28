package com.pineaple.pineaple.auth.controllers;

import com.pineaple.pineaple.auth.dto.AuthResponse;
import com.pineaple.pineaple.auth.dto.AuthResult;
import com.pineaple.pineaple.auth.dto.LoginRequest;
import com.pineaple.pineaple.auth.dto.RegisterRequest;
import com.pineaple.pineaple.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result =  authService.login(request.email(), request.password());
    return ResponseEntity.ok(AuthResponse.of(result.accessToken(), result.refreshToken(), result.expiresIn()));
    }

    @PostMapping("/register")

    public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest request){
        AuthResult result = authService.register(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.of(result.accessToken(), result.refreshToken(), result.expiresIn()));}
}

