/**
 * Swagger/OpenAPI configuration for API documentation.
 */
package com.mercadoclone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:3001}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MercadoClone API")
                        .description("REST API for Mercado Livre clone")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MercadoClone Team Case")
                                .email("wg.o.costa@gmail.com")
                                .url("https://meli-prototype-qpwg8zdib-wagner-oliveira-da-costas-projects.vercel.app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}