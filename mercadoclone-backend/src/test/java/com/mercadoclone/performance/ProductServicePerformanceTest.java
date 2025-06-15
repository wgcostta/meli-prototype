package com.mercadoclone.performance;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.dto.request.FilterRequest;
import com.mercadoclone.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Performance Tests")
class ProductServicePerformanceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;
    private List<ProductEntity> largeProductList;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        largeProductList = createLargeProductList(10000);
    }

    @Test
    @DisplayName("Should handle large product list retrieval within acceptable time")
    void shouldHandleLargeProductListRetrievalWithinAcceptableTime() {
        // Given
        when(productRepository.findAll()).thenReturn(largeProductList);

        StopWatch stopWatch = new StopWatch();
        FilterRequest filter = new FilterRequest(null, null, null, null, null, null, null, null);

        // When
        stopWatch.start();
        List<ProductEntity> result = productService.findAll(filter);
        stopWatch.stop();

        // Then
        assertThat(result).hasSize(10000);
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(1000); // Should complete within 1 second
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should handle concurrent findById requests efficiently")
    void shouldHandleConcurrentFindByIdRequestsEfficiently() throws Exception {
        // Given
        ProductEntity product = createSampleProduct("concurrent-001");
        when(productRepository.findById("concurrent-001")).thenReturn(Optional.of(product));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();
        List<CompletableFuture<ProductEntity>> futures = IntStream.range(0, 100)
                .mapToObj(i -> CompletableFuture.supplyAsync(() ->
                        productService.findById("concurrent-001"), executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(5000); // Should complete within 5 seconds
        futures.forEach(future -> {
            assertThat(future.join().getId()).isEqualTo("concurrent-001");
        });

        executor.shutdown();
    }

    @Test
    @DisplayName("Should handle multiple search operations efficiently")
    void shouldHandleMultipleSearchOperationsEfficiently() {
        // Given
        List<ProductEntity> searchResults = largeProductList.subList(0, 100);
        when(productRepository.findBySearchTerm(any())).thenReturn(searchResults);

        StopWatch stopWatch = new StopWatch();
        String[] searchTerms = {"smartphone", "laptop", "tablet", "phone", "computer"};

        // When
        stopWatch.start();
        for (String term : searchTerms) {
            List<ProductEntity> result = productService.searchProducts(term);
            assertThat(result).hasSize(100);
        }
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(500); // Should complete within 500ms
        verify(productRepository, times(5)).findBySearchTerm(any());
    }

    @Test
    @DisplayName("Should handle stress test with high volume of operations")
    void shouldHandleStressTestWithHighVolumeOfOperations() {
        // Given
        ProductEntity product = createSampleProduct("stress-001");
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.count()).thenReturn(10000L);

        StopWatch stopWatch = new StopWatch();
        int numberOfOperations = 1000;

        // When
        stopWatch.start();
        for (int i = 0; i < numberOfOperations; i++) {
            // Mix different operations
            switch (i % 3) {
                case 0 -> productService.findById("stress-001");
                case 1 -> productService.productExists("stress-001");
                case 2 -> productService.getTotalProductCount();
            }
        }
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(2000); // Should complete within 2 seconds
    }

    @Test
    @DisplayName("Should handle memory-intensive filtering operations")
    void shouldHandleMemoryIntensiveFilteringOperations() {
        // Given
        when(productRepository.findByCategory(any())).thenReturn(largeProductList.subList(0, 1000));
        when(productRepository.findByBrand(any())).thenReturn(largeProductList.subList(0, 500));
        when(productRepository.findByPriceRange(any(), any())).thenReturn(largeProductList.subList(0, 2000));

        StopWatch stopWatch = new StopWatch();

        // When
        stopWatch.start();

        // Perform multiple filtering operations
        productService.getProductsByCategory("electronics");
        productService.getProductsByBrand("Samsung");
        productService.getProductsByPriceRange(100.0, 1000.0);

        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(1000); // Should complete within 1 second
    }

    @Test
    @DisplayName("Should maintain performance with repeated cache-like operations")
    void shouldMaintainPerformanceWithRepeatedCacheLikeOperations() {
        // Given
        ProductEntity product = createSampleProduct("cache-001");
        when(productRepository.findById("cache-001")).thenReturn(Optional.of(product));

        StopWatch stopWatch = new StopWatch();
        int repetitions = 1000;

        // When
        stopWatch.start();
        for (int i = 0; i < repetitions; i++) {
            ProductEntity result = productService.findById("cache-001");
            assertThat(result.getId()).isEqualTo("cache-001");
        }
        stopWatch.stop();

        // Then
        assertThat(stopWatch.getTotalTimeMillis()).isLessThan(1000); // Should complete within 1 second
        verify(productRepository, times(repetitions)).findById("cache-001");
    }

    private List<ProductEntity> createLargeProductList(int size) {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            products.add(createSampleProduct("product-" + String.format("%05d", i)));
        }
        return products;
    }

    private ProductEntity createSampleProduct(String id) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setTitle("Product " + id);
        product.setDescription("Description for " + id);
        product.setBrand("Brand" + (id.hashCode() % 10));
        return product;
    }
}