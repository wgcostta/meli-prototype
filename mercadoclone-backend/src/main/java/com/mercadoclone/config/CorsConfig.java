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
 * Configuração CORS para permitir requisições do frontend em todos os ambientes.
 * Configuração que funciona tanto em desenvolvimento quanto em produção.
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
            // Produção: URLs específicas (sem wildcard)
            configuration.setAllowedOrigins(Arrays.asList(
                    "https://seu-frontend.netlify.app",
                    "https://seu-frontend.vercel.app",
                    "https://meli-backend-33b27898349d.herokuapp.com",
                    "https://meli-prototype-qpwg8zdib-wagner-oliveira-da-costas-projects.vercel.app"
            ));
        } else {
            // Desenvolvimento e Teste: patterns específicos (sem *)
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

        // Configuração separada para endpoints que não precisam de credenciais
        CorsConfiguration publicConfiguration = new CorsConfiguration();
        publicConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        publicConfiguration.setAllowedMethods(Arrays.asList("GET", "OPTIONS"));
        publicConfiguration.setAllowedHeaders(Arrays.asList("*"));
        publicConfiguration.setAllowCredentials(false); // SEM credenciais
        publicConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/actuator/health", publicConfiguration);
        source.registerCorsConfiguration("/actuator/**", publicConfiguration);

        // Swagger apenas em não-produção
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