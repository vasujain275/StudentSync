package me.vasujain.studentsyncapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.dto.RefreshTokenRequest;
import me.vasujain.studentsyncapi.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication-related endpoints.
 * All endpoints are prefixed with /auth due to base path configuration.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Handles user login requests.
     * This endpoint authenticates users and provides access and refresh tokens.
     *
     * Example request:
     * POST /api/v1/auth/login
     * {
     *     "username": "john_doe",
     *     "password": "password123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        AuthenticationResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles token refresh requests.
     * Users can use their refresh token to obtain a new access token when it expires.
     *
     * Example request:
     * POST /api/v1/auth/refresh
     * {
     *     "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
     * }
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshRequest
    ) {
        AuthenticationResponse response = authenticationService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user logout requests.
     * Invalidates the refresh token for the current user.
     * The client should also remove the tokens from their storage.
     *
     * Example request:
     * POST /api/v1/auth/logout
     * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader
    ) {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.substring(7); // Remove "Bearer " prefix
        authenticationService.logout(jwt);
        return ResponseEntity.ok().build();
    }

    /**
     * Simple endpoint to validate token and check if user is authenticated.
     * Useful for client-side authentication status verification.
     *
     * Example request:
     * GET /api/v1/auth/validate
     * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
     */
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken() {
        // If this endpoint is reached, it means the JWT filter authenticated the token
        return ResponseEntity.ok().build();
    }
}