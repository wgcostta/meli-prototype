/**
 * Testes de integração para ProductController.
 */
package com.mercadoclone.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Product Integration Tests")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should load and return actual product from JSON file")
    void shouldLoadAndReturnActualProductFromJsonFile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/product-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is("product-001")))
                .andExpect(jsonPath("$.data.title", notNullValue()))
                .andExpect(jsonPath("$.data.price.current", notNullValue()))
                .andExpect(jsonPath("$.data.images", not(empty())))
                .andExpect(jsonPath("$.data.category", notNullValue()));
    }

    @Test
    @DisplayName("Should return multiple products from JSON file")
    void shouldReturnMultipleProductsFromJsonFile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data[0].id", notNullValue()))
                .andExpect(jsonPath("$.data[0].title", notNullValue()));
    }

    @Test
    @DisplayName("Should filter products by category correctly")
    void shouldFilterProductsByCategoryCorrectly() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/category/cat-electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should search products by term successfully")
    void shouldSearchProductsByTermSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/search").param("q", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should return CORS headers correctly")
    void shouldReturnCorsHeadersCorrectly() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }
}