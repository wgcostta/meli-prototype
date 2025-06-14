/**
 * Configuração Spring para Cucumber.
 */
package com.mercadoclone.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

/**
 * Configuração base que garante que o Spring Context seja carregado
 * corretamente para os testes Cucumber.
 */
@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.mercadoclone.MercadoCloneApplication.class
)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @LocalServerPort
    protected int port;

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Retorna a URL base para os testes.
     */
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}