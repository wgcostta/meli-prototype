/**
 * Testes unitários para ProductService.
 */
package com.mercadoclone.service;

import com.mercadoclone.domain.entity.CategoryEntity;
import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductEntity sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = createSampleProduct();
    }

    @Test
    @DisplayName("Should return product when found by valid ID")
    void shouldReturnProductWhenFoundByValidId() {
        // Given
        String productId = "product-001";
        when(productRepository.findById(productId)).thenReturn(Optional.of(sampleProduct));

        // When
        ProductEntity result = productService.getProductById(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        String productId = "non-existent";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with ID: " + productId);

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null product ID")
    void shouldThrowExceptionForNullProductId() {
        // When & Then
        assertThatThrownBy(() -> productService.getProductById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product ID cannot be null or blank");

        verify(productRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for blank product ID")
    void shouldThrowExceptionForBlankProductId() {
        // When & Then
        assertThatThrownBy(() -> productService.getProductById("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product ID cannot be null or blank");

        verify(productRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should return all products successfully")
    void shouldReturnAllProductsSuccessfully() {
        // Given
        List<ProductEntity> expectedProducts = Arrays.asList(sampleProduct, createAnotherProduct());
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<ProductEntity> result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(sampleProduct);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should return products by category successfully")
    void shouldReturnProductsByCategorySuccessfully() {
        // Given
        String categoryId = "cat-electronics";
        List<ProductEntity> expectedProducts = Arrays.asList(sampleProduct);
        when(productRepository.findByCategory(categoryId)).thenReturn(expectedProducts);

        // When
        List<ProductEntity> result = productService.getProductsByCategory(categoryId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory().id()).isEqualTo(categoryId);
        verify(productRepository).findByCategory(categoryId);
    }

    @Test
    @DisplayName("Should validate price range correctly")
    void shouldValidatePriceRangeCorrectly() {
        // When & Then
        assertThatThrownBy(() -> productService.getProductsByPriceRange(null, 100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price range values cannot be null");

        assertThatThrownBy(() -> productService.getProductsByPriceRange(-10.0, 100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price values cannot be negative");

        assertThatThrownBy(() -> productService.getProductsByPriceRange(200.0, 100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Minimum price cannot be greater than maximum price");
    }

    private ProductEntity createSampleProduct() {
        ProductEntity product = new ProductEntity("product-001", "Sample Product", "Sample Description");
        CategoryEntity category = new CategoryEntity("cat-electronics", "Electronics", Arrays.asList("Electronics"));
        product.setCategory(category);
        return product;
    }

    private ProductEntity createAnotherProduct() {
        return new ProductEntity("product-002", "Another Product", "Another Description");
    }
}


