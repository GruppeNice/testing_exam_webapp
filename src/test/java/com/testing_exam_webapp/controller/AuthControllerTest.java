package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.LoginRequest;
import com.testing_exam_webapp.dto.LoginResponse;
import com.testing_exam_webapp.dto.RegisterRequest;
import com.testing_exam_webapp.model.mysql.User;
import com.testing_exam_webapp.model.types.Role;
import com.testing_exam_webapp.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test suite for AuthController.
 * Tests HTTP endpoints for authentication: login and register.
 */
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
        
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
        
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password123");
    }

    @Test
    @DisplayName("login - Valid credentials - Returns OK with token")
    void login_ValidCredentials_ReturnsOk() {
        LoginResponse loginResponse = new LoginResponse("jwt-token-123", "USER");
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token-123", response.getBody().getToken());
        assertEquals("USER", response.getBody().getRole());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("login - Service throws exception - Exception propagated")
    void login_ServiceThrowsException_ExceptionPropagated() {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("register - Valid request - Returns CREATED with user")
    void register_ValidRequest_ReturnsCreated() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername("newuser");
        user.setPassword("hashed-password");
        user.setRole(Role.USER);
        
        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        ResponseEntity<User> response = authController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals(Role.USER, response.getBody().getRole());
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    @DisplayName("register - Service throws exception - Exception propagated")
    void register_ServiceThrowsException_ExceptionPropagated() {
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("Registration failed"));

        assertThrows(RuntimeException.class, () -> authController.register(registerRequest));
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    @DisplayName("login - Null request body - Handles gracefully")
    void login_NullRequestBody_HandlesGracefully() {
        when(authService.login(null))
                .thenThrow(new IllegalArgumentException("Login request cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> authController.login(null));
    }

    @Test
    @DisplayName("register - Null request body - Handles gracefully")
    void register_NullRequestBody_HandlesGracefully() {
        when(authService.register(null))
                .thenThrow(new IllegalArgumentException("Register request cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> authController.register(null));
    }
}

