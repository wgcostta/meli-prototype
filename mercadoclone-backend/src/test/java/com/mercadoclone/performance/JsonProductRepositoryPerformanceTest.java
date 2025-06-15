package com.mercadoclone.performance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.infrastructure.persistence.JsonProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JsonProductRepository Performance Tests")
class JsonProductRepositoryPerformanceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private JsonProductRepository repository;
    private List<ProductEntity> largeProductDataset;

    @BeforeEach
    void setUp() {
        repository = new JsonProductRepository(
                objectMapper,
                resourceLoader,
                "classpath:data/products.json",
                "http://localhost:3001/images/products"
        );
        largeProductDataset = createLargeProductDataset(50000);
    }

    @Test
    @DisplayName("Should load large dataset within acceptable time")
    void shouldLoadLargeDatasetWithinAcceptableTime() throws IOException {
        // Given
        when(resourceLoader.getResource(any())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class)))
                .thenReturn(largeProductDataset);

        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();
        repository.loadData();
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(5000); // Should load within 5 seconds
        assertThat(repository.getCacheSize()).isEqualTo(50000);
    }

    @Test
    @DisplayName("Should perform concurrent search operations efficiently")
    void shouldPerformConcurrentSearchOperationsEfficiently() throws IOException {
        // Given
        setupMockData();
        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();

        List<String> searchTerms = List.of("smartphone", "laptop", "tablet", "phone", "computer");
        searchTerms.parallelStream().forEach(term -> {
            List<ProductEntity> results = repository.findBySearchTerm(term);
            assertThat(results).isNotNull();
        });

        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(2000); // Should complete within 2 seconds
    }

    @Test
    @DisplayName("Should handle bulk ID lookups efficiently")
    void shouldHandleBulkIdLookupsEfficiently() throws IOException {
        // Given
        setupMockData();
        List<String> productIds = IntStream.range(0, 1000)
                .mapToObj(i -> "product-" + String.format("%05d", i))
                .toList();

        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();
        productIds.forEach(id -> repository.findById(id));
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(1000); // Should complete within 1 second
    }

    @Test
    @DisplayName("Should maintain performance with complex filtering operations")
    void shouldMaintainPerformanceWithComplexFilteringOperations() throws IOException {
        // Given
        setupMockData();
        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();

        // Perform various filtering operations
        repository.findByCategory("electronics");
        repository.findByBrand("Samsung");
        repository.findByPriceRange(100.0, 1000.0);
        repository.findAvailableProducts();
        repository.findProductsWithDiscount();

        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(500); // Should complete within 500ms
    }

    @Test
    @DisplayName("Should handle memory stress test with large result sets")
    void shouldHandleMemoryStressTestWithLargeResultSets() throws IOException {
        // Given
        setupMockData();

        // When & Then
        assertThatCode(() -> {
            // Perform operations that return large result sets
            for (int i = 0; i < 100; i++) {
                List<ProductEntity> allProducts = repository.findAll();
                assertThat(allProducts).isNotNull();

                // Force garbage collection to test memory handling
                if (i % 10 == 0) {
                    System.gc();
                }
            }
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should reload data efficiently without memory leaks")
    void shouldReloadDataEfficientlyWithoutMemoryLeaks() throws IOException {
        // Given
        when(resourceLoader.getResource(any())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class)))
                .thenReturn(largeProductDataset);

        repository.loadData(); // Initial load

        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            repository.reloadData();
            assertThat(repository.getCacheSize()).isEqualTo(50000);
        }
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(10000); // Should complete within 10 seconds
    }

    @Test
    @DisplayName("Should validate cache efficiency with repeated access")
    void shouldValidateCacheEfficiencyWithRepeatedAccess() throws IOException {
        // Given
        setupMockData();
        String testId = "product-00001";

        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            repository.findById(testId);
            repository.existsById(testId);
        }
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(1000); // Should complete within 1 second
    }

    private void setupMockData() throws IOException {
        when(resourceLoader.getResource(any())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class)))
                .thenReturn(largeProductDataset);

        repository.loadData();

        // Set up the cache manually for testing
        ConcurrentHashMap<String, ProductEntity> cache = new ConcurrentHashMap<>();
        largeProductDataset.forEach(product -> cache.put(product.getId(), product));

        ReflectionTestUtils.setField(repository, "productsCache", cache);
        ReflectionTestUtils.setField(repository, "dataLoaded", true);
    }

    private List<ProductEntity> createLargeProductDataset(int size) {
        List<ProductEntity> products = new ArrayList<>();
        String[] categories = {"electronics", "clothing", "books", "home", "sports"};
        String[] brands = {"Samsung", "Apple", "Sony", "LG", "HP", "Dell", "Nike", "Adidas"};

        for (int i = 0; i < size; i++) {
            ProductEntity product = new ProductEntity();
            product.setId("product-" + String.format("%05d", i));
            product.setTitle("Product " + i);
            product.setDescription("Description for product " + i);
            product.setBrand(brands[i % brands.length]);
            // Add more fields as needed for realistic testing
            products.add(product);
        }
        return products;
    }
}