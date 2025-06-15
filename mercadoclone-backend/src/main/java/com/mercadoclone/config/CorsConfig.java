package com.mercadoclone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * CORS configuration to allow frontend requests in all environments.
 * Configuration that works both in development and production.
 *
 * @author MercadoClone Team
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if ("prod".equals(activeProfile)) {
            // Production: specific URLs (without wildcard)
            configuration.setAllowedOrigins(Arrays.asList(
                    "https://seu-frontend.netlify.app",
                    "https://seu-frontend.vercel.app",
                    "https://meli-backend-33b27898349d.herokuapp.com",
                    "https://meli-prototype-qpwg8zdib-wagner-oliveira-da-costas-projects.vercel.app"
            ));
        } else {
            // Development and Test: specific patterns (without *)
            configuration.setAllowedOriginPatterns(Arrays.asList(
                    "http://localhost:[*]",
                    "http://127.0.0.1:[*]",
                    "https://localhost:[*]",
                    "https://127.0.0.1:[*]"
            ));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        // Separate configuration for endpoints that don't need credentials
        CorsConfiguration publicConfiguration = new CorsConfiguration();
        publicConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        publicConfiguration.setAllowedMethods(Arrays.asList("GET", "OPTIONS"));
        publicConfiguration.setAllowedHeaders(Arrays.asList("*"));
        publicConfiguration.setAllowCredentials(false); // NO credentials
        publicConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/actuator/health", publicConfiguration);
        source.registerCorsConfiguration("/actuator/**", publicConfiguration);

        // Swagger only in non-production
        if (!"prod".equals(activeProfile)) {
            source.registerCorsConfiguration("/swagger-ui/**", publicConfiguration);
            source.registerCorsConfiguration("/v3/api-docs/**", publicConfiguration);
        }

        return source;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}