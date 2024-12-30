package me.vasujain.studentsyncapi.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.dto.AuthenticationResponse;
import me.vasujain.studentsyncapi.dto.LoginRequest;
import me.vasujain.studentsyncapi.dto.RefreshTokenRequest;
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

        // Set secure cookie
        setSecureCookie(response, "accessToken", accessToken);

        return buildAuthenticationResponse(user, refreshToken);
    }

    /**
     * Refreshes the access token and updates the HTTP-only cookie.
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletResponse response) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username);
        UserDetails userPrincipal = new UserPrincipal(user);

        // Validate refresh token
        if (!refreshToken.equals(user.getRefreshToken()) || !jwtService.isTokenValid(refreshToken, userPrincipal)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);

        // Update refresh token
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        // Update cookie
        setSecureCookie(response, "accessToken", newAccessToken);

        return buildAuthenticationResponse(user, newRefreshToken);
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

        // Clear cookie
        clearCookie(response, "accessToken");
    }

    /**
     * Sets a secure HTTP-only cookie with appropriate security attributes.
     */
    private void setSecureCookie(HttpServletResponse response, String name, String value) {
        boolean isDevEnvironment = "dev".equals(activeProfile);

        // Build the cookie with basic attributes
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(!isDevEnvironment); // Only false in development
        cookie.setPath("/");
        cookie.setMaxAge((int) (accessTokenExpiration/60));

        // Special handling for localhost
        if (!isDevEnvironment && cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }

        // Add the cookie to the response
        response.addCookie(cookie);

        // Set additional attributes through header for broader compatibility
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(String.format("%s=%s", name, value));
        cookieHeader.append("; Path=/");
        cookieHeader.append("; HttpOnly");

        if (!isDevEnvironment) {
            cookieHeader.append("; Secure");
            cookieHeader.append("; SameSite=Strict");
            if (cookieDomain != null) {
                cookieHeader.append("; Domain=").append(cookieDomain);
            }
        } else {
            cookieHeader.append("; SameSite=Lax");
        }

        response.setHeader("Set-Cookie", cookieHeader.toString());
    }

    private void clearCookie(HttpServletResponse response, String name) {
        boolean isDevEnvironment = "dev".equals(activeProfile);

        // Build the cookie for clearing
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(!isDevEnvironment);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        if (!isDevEnvironment && cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }

        response.addCookie(cookie);

        // Set additional attributes through header
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(String.format("%s=", name));
        cookieHeader.append("; Path=/");
        cookieHeader.append("; HttpOnly");
        cookieHeader.append("; Max-Age=0");

        if (!isDevEnvironment) {
            cookieHeader.append("; Secure");
            cookieHeader.append("; SameSite=Strict");
            if (cookieDomain != null) {
                cookieHeader.append("; Domain=").append(cookieDomain);
            }
        } else {
            cookieHeader.append("; SameSite=Lax");
        }

        response.setHeader("Set-Cookie", cookieHeader.toString());
    }

    private AuthenticationResponse buildAuthenticationResponse(User user, String refreshToken) {
        return AuthenticationResponse.builder()
                .refreshToken(refreshToken)
                .userRole(user.getRole().name())
                .username(user.getUsername())
                .build();
    }
}