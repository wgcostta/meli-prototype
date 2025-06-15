package com.mercadoclone.bdd;

import com.mercadoclone.MeliCloneApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MeliCloneApplication.class
)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @LocalServerPort
    protected int port;

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}