package me.vasujain.studentsyncapi.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.exception.InvalidTokenException;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.model.UserPrincipal;
import me.vasujain.studentsyncapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @Value("${app.cookie.secure}")
    private boolean secureCookie;

    @Value("${app.cookie.same-site}")
    private String sameSite;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * Authenticates a user and sets up their session with secure HTTP-only cookies.
     */
    public AuthenticationResponse login(LoginRequest request, HttpServletResponse response) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername());
        UserDetails userPrincipal = new UserPrincipal(user);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        // Store refresh token
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Set secure cookies
        setCookie(response, "accessToken", accessToken, accessTokenExpiration);
        setCookie(response, "refreshToken", refreshToken, refreshTokenExpiration);

        return buildAuthenticationResponse(user);
    }

    /**
     * Refreshes the access token and updates the HTTP-only cookie.
     */
    public AuthenticationResponse refreshToken(String refreshToken, HttpServletResponse response) {
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username);
        UserDetails userPrincipal = new UserPrincipal(user);

        // Validate refresh token
        assert refreshToken != null;
        if (!refreshToken.equals(user.getRefreshToken()) || !jwtService.isTokenValid(refreshToken, userPrincipal)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);

        // Update refresh token
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        // Set secure cookies
        setCookie(response, "accessToken", newAccessToken, accessTokenExpiration);
        setCookie(response, "refreshToken", newRefreshToken, refreshTokenExpiration);

        return buildAuthenticationResponse(user);
    }

    /**
     * Logs out the user by clearing their session cookies and refresh token.
     */
    public void logout(String jwt, HttpServletResponse response) {
        String username = jwtService.extractUsername(jwt);
        User user = userRepository.findByUsername(username);

        // Clear refresh token
        user.setRefreshToken(null);
        userRepository.save(user);

        // Clear cookies
        clearCookie(response, "accessToken");
        clearCookie(response, "refreshToken");
    }

    /**
     * Helper method to add cookies with domain and other secure attributes.
     */
    private void setCookie(HttpServletResponse response, String name, String value, long expiry) {
        boolean isDevEnvironment = "dev".equals(activeProfile);

        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(!isDevEnvironment);  // Ensure the cookie is only secure in production
        cookie.setPath("/");  // Ensure it works across the entire domain
        cookie.setMaxAge((int) expiry);  // Set expiration time in seconds
        cookie.setDomain(cookieDomain);

        // Set SameSite attribute for security
        if (!isDevEnvironment) {
            cookie.setHttpOnly(true);
        }

        // Add cookie to response
        response.addCookie(cookie);
    }

    /**
     * Helper method to clear cookies (logout).
     */
    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);  // Set cookie value to null
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // Secure cookie
        cookie.setPath("/");  // Ensure it works across the entire domain
        cookie.setMaxAge(0);  // Expire immediately

        // Set domain attribute if necessary
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }

        // Clear the cookie from the browser
        response.addCookie(cookie);
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        return AuthenticationResponse.builder()
                .userRole(user.getRole().name())
                .username(user.getUsername())
                .build();
    }
}
