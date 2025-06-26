package org.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for CORS (Cross-Origin Resource Sharing) settings.
 * Configures CORS policy to allow cross-origin requests from any origin
 * with common HTTP methods and headers.
 */
@Configuration
public class CustomCorsFilterConfig {

    /**
     * Creates a CORS configuration source with permissive settings.
     * Allows requests from any origin with common HTTP methods and headers.
     * 
     * @return CorsConfigurationSource with configured CORS settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Creates a CORS filter using the configured CORS configuration source.
     * 
     * @return CorsFilter configured with the CORS settings
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
