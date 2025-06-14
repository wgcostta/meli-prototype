package com.mercadoclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação MercadoClone Backend.
 *
 * Esta aplicação implementa uma API REST para um clone do Mercado Livre,
 * seguindo os princípios SOLID e Clean Code.
 *
 * @author MercadoClone Team
 * @version 1.0.0
 */
@SpringBootApplication
public class MercadoCloneApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot.
     *
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(MercadoCloneApplication.class, args);
    }
}