/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 */
package com.mercadoclone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
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
                        .description("API REST para clone do Mercado Livre")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MercadoClone Team")
                                .email("team@mercadoclone.com")
                                .url("https://github.com/mercadoclone"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://your-app.netlify.app")
                                .description("Servidor de Produção")
                ));
    }
}