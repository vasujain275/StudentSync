package me.vasujain.studentsyncapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for JWT-based authentication in the Spring Security filter chain.
 * Extends OncePerRequestFilter to guarantee it is executed only once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        // If no Authorization header or not a Bearer token, continue to the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the JWT token (remove "Bearer " prefix)
            final String jwt = authHeader.substring(7);

            // Extract username from the JWT token
            final String username = jwtService.extractUsername(jwt);

            // Only proceed with authentication if:
            // 1. Username was successfully extracted from token
            // 2. User isn't already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token against the user details
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // credentials (null since we don't need them after authentication)
                            userDetails.getAuthorities() // user's granted authorities/roles
                    );

                    // Add request details to authentication token
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Update Security Context with the new authentication token
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Log the error (you might want to use a proper logging framework)
            System.err.println("JWT Authentication error: " + e.getMessage());

            // Continue the filter chain even if authentication fails
            // Security context remains unchanged, so the request will still be unauthorized
            filterChain.doFilter(request, response);
        }
    }
}