package com.mercadoclone.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoclone.domain.entity.*;
import com.mercadoclone.exception.DataLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JsonProductRepository Tests")
class JsonProductRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private JsonProductRepository repository;
    private List<ProductEntity> mockProducts;

    private static final String PRODUCTS_FILE_PATH = "classpath:data/products.json";
    private static final String IMAGES_BASE_URL = "http://localhost:3001/images/products";

    @BeforeEach
    void setUp() {
        repository = new JsonProductRepository(
                objectMapper,
                resourceLoader,
                PRODUCTS_FILE_PATH,
                IMAGES_BASE_URL
        );

        mockProducts = createMockProducts();
    }

    @Nested
    @DisplayName("Data Loading Tests")
    class DataLoadingTests {

        @Test
        @DisplayName("Should load data successfully")
        void shouldLoadDataSuccessfully() throws IOException {
            // Given
            String jsonData = "[{\"id\":\"1\",\"title\":\"Product 1\"}]";
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());

            when(resourceLoader.getResource(PRODUCTS_FILE_PATH)).thenReturn(resource);
            when(resource.exists()).thenReturn(true);
            when(resource.getInputStream()).thenReturn(inputStream);
            when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                    .thenReturn(mockProducts);

            // When
            repository.loadData();

            // Then
            assertThat(repository.getCacheSize()).isEqualTo(mockProducts.size());
            verify(objectMapper).readValue(any(InputStream.class), any(TypeReference.class));
        }

        @Test
        @DisplayName("Should throw exception when file not found")
        void shouldThrowExceptionWhenFileNotFound() {
            // Given
            when(resourceLoader.getResource(PRODUCTS_FILE_PATH)).thenReturn(resource);
            when(resource.exists()).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> repository.loadData())
                    .isInstanceOf(DataLoadException.class)
                    .hasMessageContaining("Products file not found");
        }

        @Test
        @DisplayName("Should throw exception when IOException occurs")
        void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
            // Given
            when(resourceLoader.getResource(PRODUCTS_FILE_PATH)).thenReturn(resource);
            when(resource.exists()).thenReturn(true);
            when(resource.getInputStream()).thenThrow(new IOException("File read error"));

            // When & Then
            assertThatThrownBy(() -> repository.loadData())
                    .isInstanceOf(DataLoadException.class)
                    .hasMessageContaining("Failed to load products data");
        }

        @Test
        @DisplayName("Should process product images correctly")
        void shouldProcessProductImagesCorrectly() throws IOException {
            // Given
            ProductEntity productWithRelativeUrl = createProductWithImages("product.jpg");
            ProductEntity productWithAbsoluteUrl = createProductWithImages("http://example.com/image.jpg");
            List<ProductEntity> products = Arrays.asList(productWithRelativeUrl, productWithAbsoluteUrl);

            String jsonData = "[]";
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());

            when(resourceLoader.getResource(PRODUCTS_FILE_PATH)).thenReturn(resource);
            when(resource.exists()).thenReturn(true);
            when(resource.getInputStream()).thenReturn(inputStream);
            when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                    .thenReturn(products);

            // When
            repository.loadData();

            // Then
            assertThat(productWithRelativeUrl.getImages().get(0).getUrl())
                    .isEqualTo(IMAGES_BASE_URL + "/product.jpg");
            assertThat(productWithAbsoluteUrl.getImages().get(0).getUrl())
                    .isEqualTo("http://example.com/image.jpg");
        }
    }

    @Nested
    @DisplayName("Find Operations Tests")
    class FindOperationsTests {

        @BeforeEach
        void setUpData() {
            loadMockData();
        }

        @Test
        @DisplayName("Should find product by ID")
        void shouldFindProductById() {
            // When
            Optional<ProductEntity> result = repository.findById("1");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo("1");
            assertThat(result.get().getTitle()).isEqualTo("Smartphone Samsung");
        }

        @Test
        @DisplayName("Should return empty when product not found")
        void shouldReturnEmptyWhenProductNotFound() {
            // When
            Optional<ProductEntity> result = repository.findById("999");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should find all products")
        void shouldFindAllProducts() {
            // When
            List<ProductEntity> result = repository.findAll();

            // Then
            assertThat(result).hasSize(mockProducts.size());
            assertThat(result).extracting(ProductEntity::getId)
                    .containsExactlyInAnyOrder("1", "2", "3");
        }

        @Test
        @DisplayName("Should find products by category")
        void shouldFindProductsByCategory() {
            // When
            List<ProductEntity> result = repository.findByCategory("electronics");

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(ProductEntity::getId)
                    .containsExactlyInAnyOrder("1", "2");
        }

        @Test
        @DisplayName("Should find products by brand")
        void shouldFindProductsByBrand() {
            // When
            List<ProductEntity> result = repository.findByBrand("Samsung");

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo("1");
        }

        @Test
        @DisplayName("Should find products by search term in title")
        void shouldFindProductsBySearchTermInTitle() {
            // When
            List<ProductEntity> result = repository.findBySearchTerm("smartphone");

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo("1");
        }

        @Test
        @DisplayName("Should find products by search term in description")
        void shouldFindProductsBySearchTermInDescription() {
            // When
            List<ProductEntity> result = repository.findBySearchTerm("avançado");

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo("1");
        }

        @Test
        @DisplayName("Should find available products only")
        void shouldFindAvailableProductsOnly() {
            // When
            List<ProductEntity> result = repository.findAvailableProducts();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(ProductEntity::getId)
                    .containsExactlyInAnyOrder("1", "2");
        }

        @Test
        @DisplayName("Should find products with discount")
        void shouldFindProductsWithDiscount() {
            // When
            List<ProductEntity> result = repository.findProductsWithDiscount();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo("1");
        }

        @Test
        @DisplayName("Should find products by price range")
        void shouldFindProductsByPriceRange() {
            // When
            List<ProductEntity> result = repository.findByPriceRange(500.0, 1500.0);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(ProductEntity::getId)
                    .containsExactlyInAnyOrder("1", "2");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @BeforeEach
        void setUpData() {
            loadMockData();
        }

        @Test
        @DisplayName("Should throw exception for null product ID")
        void shouldThrowExceptionForNullProductId() {
            // When & Then
            assertThatThrownBy(() -> repository.findById(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Product ID cannot be null or blank");
        }

        @Test
        @DisplayName("Should throw exception for blank product ID")
        void shouldThrowExceptionForBlankProductId() {
            // When & Then
            assertThatThrownBy(() -> repository.findById("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Product ID cannot be null or blank");
        }

        @Test
        @DisplayName("Should throw exception for null category ID")
        void shouldThrowExceptionForNullCategoryId() {
            // When & Then
            assertThatThrownBy(() -> repository.findByCategory(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Category ID cannot be null or blank");
        }

        @Test
        @DisplayName("Should throw exception for null brand")
        void shouldThrowExceptionForNullBrand() {
            // When & Then
            assertThatThrownBy(() -> repository.findByBrand(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Brand cannot be null or blank");
        }

        @Test
        @DisplayName("Should throw exception for null search term")
        void shouldThrowExceptionForNullSearchTerm() {
            // When & Then
            assertThatThrownBy(() -> repository.findBySearchTerm(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Search term cannot be null or blank");
        }

        @Test
        @DisplayName("Should throw exception for null price range")
        void shouldThrowExceptionForNullPriceRange() {
            // When & Then
            assertThatThrownBy(() -> repository.findByPriceRange(null, 100.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Price range values cannot be null");
        }

        @Test
        @DisplayName("Should throw exception for negative prices")
        void shouldThrowExceptionForNegativePrices() {
            // When & Then
            assertThatThrownBy(() -> repository.findByPriceRange(-10.0, 100.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Price values cannot be negative");
        }

        @Test
        @DisplayName("Should throw exception when min price greater than max price")
        void shouldThrowExceptionWhenMinPriceGreaterThanMaxPrice() {
            // When & Then
            assertThatThrownBy(() -> repository.findByPriceRange(100.0, 50.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Minimum price cannot be greater than maximum price");
        }

        @Test
        @DisplayName("Should throw exception when data not loaded")
        void shouldThrowExceptionWhenDataNotLoaded() {
            // Given
            JsonProductRepository newRepository = new JsonProductRepository(
                    objectMapper, resourceLoader, PRODUCTS_FILE_PATH, IMAGES_BASE_URL
            );

            // When & Then
            assertThatThrownBy(() -> newRepository.findAll())
                    .isInstanceOf(DataLoadException.class)
                    .hasMessageContaining("Product data not loaded");
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityMethodsTests {

        @BeforeEach
        void setUpData() {
            loadMockData();
        }

        @Test
        @DisplayName("Should check if product exists")
        void shouldCheckIfProductExists() {
            // When & Then
            assertThat(repository.existsById("1")).isTrue();
            assertThat(repository.existsById("999")).isFalse();
        }

        @Test
        @DisplayName("Should count products correctly")
        void shouldCountProductsCorrectly() {
            // When
            long count = repository.count();

            // Then
            assertThat(count).isEqualTo(mockProducts.size());
        }

        @Test
        @DisplayName("Should get cache size correctly")
        void shouldGetCacheSizeCorrectly() {
            // When
            int cacheSize = repository.getCacheSize();

            // Then
            assertThat(cacheSize).isEqualTo(mockProducts.size());
        }

        @Test
        @DisplayName("Should reload data successfully")
        void shouldReloadDataSuccessfully() throws IOException {
            // Given
            String jsonData = "[{\"id\":\"1\",\"title\":\"Product 1\"}]";
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());

            when(resourceLoader.getResource(PRODUCTS_FILE_PATH)).thenReturn(resource);
            when(resource.exists()).thenReturn(true);
            when(resource.getInputStream()).thenReturn(inputStream);
            when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                    .thenReturn(mockProducts);

            // When
            repository.reloadData();

            // Then
            assertThat(repository.getCacheSize()).isEqualTo(mockProducts.size());
            verify(objectMapper, times(2)).readValue(any(InputStream.class), any(TypeReference.class));
        }
    }

    // Helper methods

    private void loadMockData() {
        ReflectionTestUtils.setField(repository, "dataLoaded", true);
        ReflectionTestUtils.setField(repository, "productsCache",
                mockProducts.stream().collect(
                        java.util.stream.Collectors.toConcurrentMap(
                                ProductEntity::getId,
                                product -> product
                        )
                )
        );
    }

    private List<ProductEntity> createMockProducts() {
        return Arrays.asList(
                createProduct("1", "Smartphone Samsung", "electronics", "Samsung",
                        1200.0, 1000.0, true, "Smartphone avançado"),
                createProduct("2", "Notebook Dell", "electronics", "Dell",
                        1500.0, null, true, "Notebook para trabalho"),
                createProduct("3", "Mesa de Escritório", "furniture", "IKEA",
                        300.0, null, false, "Mesa moderna")
        );
    }

    private ProductEntity createProduct(String id, String title, String categoryId,
                                        String brand, Double originalPrice, Double currentPrice,
                                        boolean available, String description) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setTitle(title);
        product.setCategory(new CategoryEntity(categoryId, categoryId.toUpperCase(), null));
        product.setBrand(brand);
        product.setDescription(description);
        product.setShortDescription(description);
        product.setStock(new StockEntity(10, 100));

        PriceEntity price = new PriceEntity();
        price.setOriginal(originalPrice);
        price.setCurrent(currentPrice != null ? currentPrice : originalPrice);
        product.setPrice(price);

        return product;
    }

    private ProductEntity createProductWithImages(String imageUrl) {
        ProductEntity product = createProduct("1", "Test Product", "test", "Test", 100.0, null, true, "Test");
        ProductImageEntity image = new ProductImageEntity();
        image.setUrl(imageUrl);
        product.setImages(Arrays.asList(image));
        return product;
    }
}