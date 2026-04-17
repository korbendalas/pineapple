package com.pineaple.pineaple.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pineaple.pineaple.auth.dto.AuthResult;
import com.pineaple.pineaple.auth.exceptions.InvalidCredentialsException;
import com.pineaple.pineaple.auth.services.AuthService;
import com.pineaple.pineaple.shared.exception.GlobalExceptionHandler;
import com.pineaple.pineaple.users.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerSecurityTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthService authService = mock(AuthService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new AuthController(authService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .setMessageConverters(new JacksonJsonHttpMessageConverter())
            .build();

    @Test
    void register_isPublic_andReturnsTokens() throws Exception {
        when(authService.register(anyString(), anyString()))
                .thenReturn(new AuthResult("access-token", "refresh-token", 900000L));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterPayload("john.doe@example.com", "TestPass123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_invalidCredentials_returnsUnauthorizedProblemDetail() throws Exception {
        when(authService.login(anyString(), anyString())).thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginPayload("john.doe@example.com", "wrong-pass"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.detail").value("Invalid email or password."));
    }

    @Test
    void register_existingUser_returnsConflictProblemDetail() throws Exception {
        when(authService.register(anyString(), anyString()))
                .thenThrow(new UserAlreadyExistsException("john.doe@example.com"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterPayload("john.doe@example.com", "TestPass123!"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"));
    }

    private record LoginPayload(String email, String password) {}

    private record RegisterPayload(String email, String password) {}
}
