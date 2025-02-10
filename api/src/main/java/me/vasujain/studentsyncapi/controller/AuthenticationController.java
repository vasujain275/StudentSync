package me.vasujain.studentsyncapi.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.service.AuthenticationService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication-related endpoints.
 * Implements secure authentication using HTTP-only cookies for access tokens.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger;

    public AuthenticationController(AuthenticationService authenticationService, Logger logger) {
        this.authenticationService = authenticationService;
        this.logger = logger;
    }


    /**
     * Handles user login requests.
     * Sets access token as HTTP-only cookie and returns user info with refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        logger.debug("Login request received for user={}", loginRequest.getUsername());
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
        logger.debug("Refresh token request received");
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
        logger.debug("Logout request received");
        if (accessToken != null) {
            authenticationService.logout(accessToken, response);
        }
        return ResponseEntity.ok().build();
    }
}