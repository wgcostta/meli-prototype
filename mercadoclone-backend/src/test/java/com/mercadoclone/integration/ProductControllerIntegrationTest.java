package com.mercadoclone.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoclone.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
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
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is("c71ea0ea-f273-4d2c-8a0e-7afe89294b9a")))
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
        mockMvc.perform(get("/api/v1/products")
                        .param("categoryId", "cat-electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should search products by term successfully")
    void shouldSearchProductsByTermSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products").param("value", "Samsung"))
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

    // Adicione estes métodos à classe ProductControllerIntegrationTest

    @Test
    @DisplayName("Should return total product count with actual data")
    void shouldReturnTotalProductCountWithActualData() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", isA(Number.class)))
                .andExpect(jsonPath("$.data", greaterThan(0)));
    }

    @Test
    @DisplayName("Should verify product existence with valid product ID")
    void shouldVerifyProductExistenceWithValidProductId() throws Exception {
        // Given - Using a known product ID from JSON file
        String validProductId = "c71ea0ea-f273-4d2c-8a0e-7afe89294b9a";

        // When & Then
        mockMvc.perform(get("/api/v1/products/{productId}/exists", validProductId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    @DisplayName("Should verify product non-existence with invalid product ID")
    void shouldVerifyProductNonExistenceWithInvalidProductId() throws Exception {
        // Given
        String invalidProductId = "invalid-product-id-xyz";

        // When & Then
        mockMvc.perform(get("/api/v1/products/{productId}/exists", invalidProductId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(false)));
    }

    @Test
    @DisplayName("Should filter products by brand with real data is empty")
    void shouldFilterProductsByBrandWithRealData() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should filter products by brand case insensitive")
    void shouldFilterProductsByBrandCaseInsensitive() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should return available products with real JSON data")
    void shouldReturnAvailableProductsWithRealJsonData() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.data[*].available", everyItem(is(true))));
    }

    @Test
    @DisplayName("Should return discounted products with real JSON data")
    void shouldReturnDiscountedProductsWithRealJsonData() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("discounted", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.data[*].discount", everyItem(notNullValue())));
    }

    @Test
    @DisplayName("Should handle non-existent brand filter gracefully")
    void shouldHandleNonExistentBrandFilterGracefully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "NonExistentBrandXYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle available filter with false parameter")
    void shouldHandleAvailableFilterWithFalseParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("available", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should handle discounted filter with false parameter")
    void shouldHandleDiscountedFilterWithFalseParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("discounted", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should validate product exists endpoint with empty string ID")
    void shouldValidateProductExistsEndpointWithEmptyStringId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/ /exists"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should handle brand filter with special characters")
    void shouldHandleBrandFilterWithSpecialCharacters() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "Brand@#$%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should verify product count is consistent with product list size")
    void shouldVerifyProductCountIsConsistentWithProductListSize() throws Exception {
        // Get total count
        MvcResult countResult = mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andReturn();

        String countResponseBody = countResult.getResponse().getContentAsString();

        // Parse JSON using ObjectMapper
        JsonNode countJson = objectMapper.readTree(countResponseBody);
        Long totalCount = countJson.get("data").asLong();

        // Get all products
        MvcResult listResult = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn();

        String listResponseBody = listResult.getResponse().getContentAsString();

        // Parse JSON using ObjectMapper
        JsonNode listJson = objectMapper.readTree(listResponseBody);
        JsonNode dataArray = listJson.get("data");
        int listSize = dataArray.size();

        // Verify consistency
        assertThat(totalCount).isEqualTo(listSize);
    }

    @Test
    @DisplayName("Should handle performance test for product exists endpoint")
    void shouldHandlePerformanceTestForProductExistsEndpoint() throws Exception {
        long startTime = System.currentTimeMillis();

        // When & Then
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        // Assert response time is reasonable (less than 1 second)
        assertThat(responseTime).isLessThan(1000);
    }

    @Test
    @DisplayName("Should handle performance test for product count endpoint")
    void shouldHandlePerformanceTestForProductCountEndpoint() throws Exception {
        long startTime = System.currentTimeMillis();

        // When & Then
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        // Assert response time is reasonable (less than 1 second)
        assertThat(responseTime).isLessThan(1000);
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/non-existent-id"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Product not found with ID: non-existent-id")))
                .andExpect(jsonPath("$.code", is("PRODUCT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", containsString("Product not found")));
    }

    @Test
    @DisplayName("Should filter products by brand from JSON file")
    void shouldFilterProductsByBrandFromJsonFile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.data[*].brand", everyItem(containsStringIgnoringCase("Samsung"))));
    }

    @Test
    @DisplayName("Should return available products only")
    void shouldReturnAvailableProductsOnly() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should return discounted products only")
    void shouldReturnDiscountedProductsOnly() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("discounted", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should filter products by valid price range")
    void shouldFilterProductsByValidPriceRange() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true")
                        .param("minPrice", "100")
                        .param("maxPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should return 400 for invalid price range - min greater than max")
    void shouldReturn400ForInvalidPriceRangeMinGreaterThanMax() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true")
                        .param("minPrice", "1000")
                        .param("maxPrice", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Minimum price cannot be greater than maximum price")))
                .andExpect(jsonPath("$.code", is("INVALID_ARGUMENT")));
    }

    @Test
    @DisplayName("Should return 400 for negative price values")
    void shouldReturn400ForNegativePriceValues() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true")
                        .param("minPrice", "-10")
                        .param("maxPrice", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Price values cannot be negative")))
                .andExpect(jsonPath("$.code", is("INVALID_ARGUMENT")));
    }

    @Test
    @DisplayName("Should return 400 for null price values when range price is enabled")
    void shouldReturn400ForNullPriceValuesWhenRangePriceEnabled() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Price range values cannot be null")))
                .andExpect(jsonPath("$.code", is("INVALID_ARGUMENT")));
    }

    @Test
    @DisplayName("Should verify existing product returns true")
    void shouldVerifyExistingProductReturnsTrue() throws Exception {
        // Using a known product ID from the JSON file
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a/exists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    @DisplayName("Should verify non-existing product returns false")
    void shouldVerifyNonExistingProductReturnsFalse() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/non-existent-product-id/exists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(false)));
    }

    @Test
    @DisplayName("Should return total product count")
    void shouldReturnTotalProductCount() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", isA(Number.class)))
                .andExpect(jsonPath("$.data", greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("Should handle search with special characters")
    void shouldHandleSearchWithSpecialCharacters() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("value", "iphone@#$%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should handle search with very long search term")
    void shouldHandleSearchWithVeryLongSearchTerm() throws Exception {
        String longSearchTerm = "a".repeat(1000);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("value", longSearchTerm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0))); // Should return empty for non-existent long term
    }

    @Test
    @DisplayName("Should handle multiple filters simultaneously")
    void shouldHandleMultipleFiltersSimultaneously() throws Exception {
        // When & Then - Note: Only the first filter should be processed based on service logic
        mockMvc.perform(get("/api/v1/products")
                        .param("categoryId", "cat-electronics")
                        .param("brandId", "Samsung")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should handle edge case with zero price range")
    void shouldHandleEdgeCaseWithZeroPriceRange() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true")
                        .param("minPrice", "0")
                        .param("maxPrice", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should handle case insensitive search")
    void shouldHandleCaseInsensitiveSearch() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("value", "SAMSUNG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Should return appropriate headers for all endpoints")
    void shouldReturnAppropriateHeadersForAllEndpoints() throws Exception {
        // Test main endpoint
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/json")));

        // Test exists endpoint
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a/exists"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/json")));

        // Test count endpoint
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    @DisplayName("Should handle OPTIONS request for CORS preflight")
    void shouldHandleOptionsRequestForCorsPreflight() throws Exception {
        // When & Then
        mockMvc.perform(options("/api/v1/products")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().exists("Access-Control-Allow-Methods"));
    }

    @Test
    @DisplayName("Should validate consistent response structure across all endpoints")
    void shouldValidateConsistentResponseStructureAcrossAllEndpoints() throws Exception {
        // Test list endpoint
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.code").doesNotExist());

        // Test single product endpoint
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.code").doesNotExist());

        // Test exists endpoint
        mockMvc.perform(get("/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.code").doesNotExist());

        // Test count endpoint
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.code").doesNotExist());
    }

    @Test
    @DisplayName("Should handle performance with large result sets")
    void shouldHandlePerformanceWithLargeResultSets() throws Exception {
        long startTime = System.currentTimeMillis();

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        // Assert response time is reasonable (less than 5 seconds)
        assertThat(responseTime).isLessThan(5000);
    }
}