package com.mercadoclone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.mapper.ProductMapper;
import com.mercadoclone.dto.response.ProductResponse;
import com.mercadoclone.exception.ProductNotFoundException;
import com.mercadoclone.service.ProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController Unit Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductUseCase productService;

    private ProductMapper productMapperInstance = Mappers.getMapper(ProductMapper.class);

    @MockBean
    private ProductMapper productMapper;

    @Test
    @DisplayName("Should return product successfully when valid ID provided")
    void shouldReturnProductSuccessfullyWhenValidIdProvided() throws Exception {
        // Given
        ProductEntity product = new ProductEntity("product-001", "Test Product", "Test Description");
        when(productService.findById("product-001")).thenReturn(product);

        // Mock the mapper to return a DTO
        ProductResponse response = productMapperInstance.toResponse(product);

        when(productMapper.toResponse(product)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/products/product-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(response.id())))
                .andExpect(jsonPath("$.data.title", is("Test Product")));
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        // Given
        when(productService.findById("non-existent"))
                .thenThrow(new ProductNotFoundException("Product not found with ID: non-existent"));

        // When & Then
        mockMvc.perform(get("/api/v1/products/non-existent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", containsString("Product not found")));
    }

    @Test
    @DisplayName("Should return all products successfully")
    void shouldReturnAllProductsSuccessfully() throws Exception {
        // Given
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity("product-001", "Product 1", "Description 1"),
                new ProductEntity("product-002", "Product 2", "Description 2")
        );

        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(products);

        // Mock the mapper to return a list of DTOs
        List<ProductResponse> responseList = productMapperInstance.toResponseList(products);
        when(productMapper.toResponseList(products)).thenReturn(responseList);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is("product-001")))
                .andExpect(jsonPath("$.data[1].id", is("product-002")));
    }

    @Test
    @DisplayName("Should return 400 for invalid price range")
    void shouldReturn400ForInvalidPriceRange() throws Exception {
        when(productService.findAllWithCommandPattern(ArgumentMatchers.any()))
                .thenThrow(new IllegalArgumentException("Invalid price range"));

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("rangePrice", "true")
                        .param("minPrice", "200")
                        .param("maxPrice", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("INVALID_ARGUMENT")));
    }

    @Test
    @DisplayName("Should handle search with empty results")
    void shouldHandleSearchWithEmptyResults() throws Exception {
        // Given
        when(productService.searchProducts("nonexistent")).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("q", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should return total product count successfully")
    void shouldReturnTotalProductCountSuccessfully() throws Exception {
        // Given
        long expectedCount = 150L;
        when(productService.getTotalProductCount()).thenReturn(expectedCount);

        // When & Then
        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(150)));
    }

    @Test
    @DisplayName("Should return true when product exists")
    void shouldReturnTrueWhenProductExists() throws Exception {
        // Given
        String productId = "existing-product-id";
        when(productService.productExists(productId)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{productId}/exists", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    @DisplayName("Should return false when product does not exist")
    void shouldReturnFalseWhenProductDoesNotExist() throws Exception {
        // Given
        String productId = "non-existing-product-id";
        when(productService.productExists(productId)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{productId}/exists", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(false)));
    }

    @Test
    @DisplayName("Should return products filtered by brand successfully")
    void shouldReturnProductsFilteredByBrandSuccessfully() throws Exception {
        // Given
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity("product-001", "Samsung Phone", "Samsung smartphone"),
                new ProductEntity("product-002", "Samsung TV", "Samsung television")
        );

        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(products);

        List<ProductResponse> responseList = productMapperInstance.toResponseList(products);
        when(productMapper.toResponseList(products)).thenReturn(responseList);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is("product-001")))
                .andExpect(jsonPath("$.data[1].id", is("product-002")));
    }

    @Test
    @DisplayName("Should return available products successfully")
    void shouldReturnAvailableProductsSuccessfully() throws Exception {
        // Given
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity("product-001", "Available Product 1", "Description 1"),
                new ProductEntity("product-002", "Available Product 2", "Description 2")
        );

        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(products);

        List<ProductResponse> responseList = productMapperInstance.toResponseList(products);
        when(productMapper.toResponseList(products)).thenReturn(responseList);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is("product-001")))
                .andExpect(jsonPath("$.data[1].id", is("product-002")));
    }

    @Test
    @DisplayName("Should return discounted products successfully")
    void shouldReturnDiscountedProductsSuccessfully() throws Exception {
        // Given
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity("product-001", "Discounted Product 1", "Product with discount"),
                new ProductEntity("product-002", "Discounted Product 2", "Another discounted product")
        );

        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(products);

        List<ProductResponse> responseList = productMapperInstance.toResponseList(products);
        when(productMapper.toResponseList(products)).thenReturn(responseList);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("discounted", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is("product-001")))
                .andExpect(jsonPath("$.data[1].id", is("product-002")));
    }

    @Test
    @DisplayName("Should handle empty brand filter results")
    void shouldHandleEmptyBrandFilterResults() throws Exception {
        // Given
        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(Arrays.asList());
        when(productMapper.toResponseList(Arrays.asList())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("brandId", "NonExistentBrand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle empty available products results")
    void shouldHandleEmptyAvailableProductsResults() throws Exception {
        // Given
        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(Arrays.asList());
        when(productMapper.toResponseList(Arrays.asList())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle empty discounted products results")
    void shouldHandleEmptyDiscountedProductsResults() throws Exception {
        // Given
        when(productService.findAllWithCommandPattern(ArgumentMatchers.any())).thenReturn(Arrays.asList());
        when(productMapper.toResponseList(Arrays.asList())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("discounted", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}