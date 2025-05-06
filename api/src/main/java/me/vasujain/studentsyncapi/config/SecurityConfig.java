package me.vasujain.studentsyncapi.config;

import lombok.RequiredArgsConstructor;
import me.vasujain.studentsyncapi.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // We inject these dependencies using constructor injection
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Configures the main security filter chain.
     * This defines how different requests should be secured and authenticated.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Define arrays of endpoints that follow the same pattern
        String[] superAdminEndpoints = {
                "/school",
                "/department",
                "/semester",
                "/course",
                "/enrollment"
        };

        String[] sharedAdminEndpoints = {
                "/grade",
                "/attendance"
        };

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/user/**",
                                "/auth/**",
                                "/health",
                                "/docs",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/openapi.yaml",
                                "/actuator/**"
                        ).permitAll()

                        // Notice endpoints
                        .requestMatchers(HttpMethod.GET, "/notice/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/notice").hasRole("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/notice/**").hasRole("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/notice/**").hasRole("SUPER_ADMIN")

                        // Super Admin controlled endpoints
                        .requestMatchers(HttpMethod.GET, Arrays.stream(superAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).authenticated()
                        .requestMatchers(HttpMethod.POST, superAdminEndpoints).hasRole("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, Arrays.stream(superAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).hasRole("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Arrays.stream(superAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).hasRole("SUPER_ADMIN")

                        // Shared Admin endpoints (ADMIN and SUPER_ADMIN)
                        .requestMatchers(HttpMethod.GET, Arrays.stream(sharedAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).authenticated()
                        .requestMatchers(HttpMethod.POST, sharedAdminEndpoints).hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, Arrays.stream(sharedAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Arrays.stream(sharedAdminEndpoints)
                                .map(path -> path + "/**")
                                .toArray(String[]::new)).hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // Role-based paths
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/superadmin/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Creates the PasswordEncoder bean for secure password hashing.
     * BCrypt is a strong hashing function designed for passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Using strength factor 12 for a good balance of security and performance
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Creates the AuthenticationProvider bean.
     * This tells Spring Security how to authenticate users.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Configure the provider with our UserDetailsService and password encoder
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Creates the AuthenticationManager bean.
     * This is the main Spring Security interface for authenticating users.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}