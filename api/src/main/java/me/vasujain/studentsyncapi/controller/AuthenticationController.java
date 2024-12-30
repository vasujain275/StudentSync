package me.vasujain.studentsyncapi.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication-related endpoints.
 * Implements secure authentication using HTTP-only cookies for access tokens.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    /**
     * Handles user login requests.
     * Sets access token as HTTP-only cookie and returns user info with refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = authenticationService.login(loginRequest, response);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Handles token refresh requests.
     * Updates the HTTP-only cookie with new access token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = authenticationService.refreshToken(refreshToken, response);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Handles user logout requests.
     * Clears the HTTP-only cookie and invalidates refresh token.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            HttpServletResponse response
    ) {
        if (accessToken != null) {
            authenticationService.logout(accessToken, response);
        }
        return ResponseEntity.ok().build();
    }
}