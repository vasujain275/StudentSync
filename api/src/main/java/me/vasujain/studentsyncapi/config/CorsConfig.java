package me.vasujain.studentsyncapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {
    private final CorsProperties corsProperties;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Split the comma-separated strings into lists
        config.setAllowedOrigins(
                Arrays.asList(corsProperties.getAllowedOrigins().split(","))
        );
        config.setAllowedMethods(
                Arrays.asList(corsProperties.getAllowedMethods().split(","))
        );
        config.setAllowedHeaders(
                Arrays.asList(corsProperties.getAllowedHeaders().split(","))
        );

        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("*", config);

        return new CorsFilter(source);
    }
}