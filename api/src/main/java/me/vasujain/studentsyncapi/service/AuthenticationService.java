package me.vasujain.studentsyncapi.service;

import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.dto.RefreshTokenRequest;
import me.vasujain.studentsyncapi.exception.InvalidTokenException;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.model.UserPrincipal;
import me.vasujain.studentsyncapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    // Using constructor injection with @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user and generates new access and refresh tokens.
     * @param request The login request containing username and password
     * @return AuthenticationResponse containing the tokens and user information
     */
    public AuthenticationResponse login(LoginRequest request) {
        // Authenticate the user with Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),  // Changed from getEmail to getUsername
                        request.getPassword()
                )
        );

        // Find the user in our database
        User user = userRepository.findByUsername(request.getUsername());  // Changed from findByEmail
        UserDetails userPrincipal = new UserPrincipal(user);

        // Generate new tokens
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        // Store the refresh token in the database
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Return the authentication response
        return buildAuthenticationResponse(user, accessToken, refreshToken);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     * @param request The refresh token request
     * @return AuthenticationResponse containing new tokens and user information
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        // Extract username from the refresh token
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        // Find the user in our database
        User user = userRepository.findByUsername(username);
        UserDetails userPrincipal = new UserPrincipal(user);

        // Verify if the provided refresh token matches the stored one
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        // Validate the refresh token
        if (jwtService.isTokenValid(refreshToken, userPrincipal)) {
            // Generate new tokens
            String newAccessToken = jwtService.generateAccessToken(userPrincipal);
            String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);

            // Update the refresh token in the database
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);

            // Return the new authentication response
            return buildAuthenticationResponse(user, newAccessToken, newRefreshToken);
        }

        throw new InvalidTokenException("Invalid refresh token");
    }

    /**
     * Handles user logout by invalidating their refresh token.
     * This prevents the refresh token from being used to obtain new access tokens.
     */
    public void logout(String jwt) {
        String username = jwtService.extractUsername(jwt);
        User user = userRepository.findByUsername(username);

        // Clear the refresh token
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    /**
     * Helper method to build the authentication response.
     * This keeps our response building logic in one place.
     */
    private AuthenticationResponse buildAuthenticationResponse(User user, String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userRole(user.getRole().name())
                .username(user.getUsername())
                .build();
    }
}