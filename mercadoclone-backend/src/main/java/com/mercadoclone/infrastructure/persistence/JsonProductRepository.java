package com.mercadoclone.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.exception.DataLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Product repository implementation that loads data from a JSON file.
 *
 * This implementation follows SOLID principles:
 * - SRP: Responsible only for product persistence
 * - OCP: Can be extended without modification
 * - LSP: Perfectly substitutes the ProductRepository interface
 * - ISP: Implements only the necessary interface
 * - DIP: Depends on abstractions (ProductRepository, ObjectMapper)
 *
 * @author MercadoClone Team
 */
@Repository
public class JsonProductRepository implements ProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(JsonProductRepository.class);

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final String productsFilePath;
    private final String imagesBaseUrl;

    // In-memory cache for better performance
    private final Map<String, ProductEntity> productsCache = new ConcurrentHashMap<>();
    private volatile boolean dataLoaded = false;

    public JsonProductRepository(
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            @Value("${app.data.products-file:classpath:data/products.json}") String productsFilePath,
            @Value("${app.images.base-url:http://localhost:3001/images/products}") String imagesBaseUrl) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.productsFilePath = productsFilePath;
        this.imagesBaseUrl = imagesBaseUrl;
    }

    /**
     * Loads data from JSON file after bean construction.
     */
    @PostConstruct
    public void loadData() {
        try {
            logger.info("Loading products data from: {}", productsFilePath);

            Resource resource = resourceLoader.getResource(productsFilePath);
            if (!resource.exists()) {
                throw new DataLoadException("Products file not found: " + productsFilePath);
            }

            try (InputStream inputStream = resource.getInputStream()) {
                List<ProductEntity> products = objectMapper.readValue(
                        inputStream,
                        new TypeReference<>() {
                        }
                );

                // Process image URLs and add to cache
                products.forEach(this::processProductImages);
                products.forEach(product -> productsCache.put(product.getId(), product));

                dataLoaded = true;
                logger.info("Successfully loaded {} products", products.size());
            }

        } catch (IOException e) {
            logger.error("Error loading products data", e);
            throw new DataLoadException("Failed to load products data", e);
        }
    }

    /**
     * Processes product image URLs, adding the base URL.
     */
    private void processProductImages(ProductEntity product) {
        if (product.getImages() != null) {
            product.getImages().forEach(image -> {
                if (image.getUrl() != null && !image.getUrl().startsWith("http")) {
                    String fullUrl = imagesBaseUrl + "/" + image.getUrl();
                    image.setUrl(fullUrl);
                }
            });
        }
    }

    @Override
    public Optional<ProductEntity> findById(String productId) {
        validateProductId(productId);
        ensureDataLoaded();

        logger.debug("Finding product by ID: {}", productId);
        return Optional.ofNullable(productsCache.get(productId));
    }

    @Override
    public List<ProductEntity> findAll() {
        ensureDataLoaded();

        logger.debug("Finding all products");
        return Collections.unmodifiableList(
                productsCache.values().stream().collect(Collectors.toList())
        );
    }

    @Override
    public List<ProductEntity> findByCategory(String categoryId) {
        validateNonBlankString(categoryId, "Category ID");
        ensureDataLoaded();

        logger.debug("Finding products by category: {}", categoryId);
        return productsCache.values().stream()
                .filter(product -> product.getCategory() != null)
                .filter(product -> categoryId.equals(product.getCategory().id()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findByBrand(String brand) {
        validateNonBlankString(brand, "Brand");
        ensureDataLoaded();

        logger.debug("Finding products by brand: {}", brand);
        return productsCache.values().stream()
                .filter(product -> brand.equalsIgnoreCase(product.getBrand()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findBySearchTerm(String searchTerm) {
        validateNonBlankString(searchTerm, "Search term");
        ensureDataLoaded();

        logger.debug("Finding products by search term: {}", searchTerm);
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        return productsCache.values().stream()
                .filter(product -> containsSearchTerm(product, lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAvailableProducts() {
        ensureDataLoaded();

        logger.debug("Finding available products");
        return productsCache.values().stream()
                .filter(ProductEntity::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String productId) {
        validateProductId(productId);
        ensureDataLoaded();

        return productsCache.containsKey(productId);
    }

    @Override
    public long count() {
        ensureDataLoaded();
        return productsCache.size();
    }

    @Override
    public List<ProductEntity> findProductsWithDiscount() {
        ensureDataLoaded();

        logger.debug("Finding products with discount");

        return productsCache.values().stream()
                .filter(ProductEntity::hasDiscount)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findByPriceRange(Double minPrice, Double maxPrice) {
        validatePriceRange(minPrice, maxPrice);
        ensureDataLoaded();

        logger.debug("Finding products by price range: {} - {}", minPrice, maxPrice);
        return productsCache.values().stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getPrice().getCurrent() != null)
                .filter(product -> {
                    Double currentPrice = product.getPrice().getCurrent();
                    return currentPrice >= minPrice && currentPrice <= maxPrice;
                })
                .collect(Collectors.toList());
    }

    /**
     * Checks if a product contains the search term in title or description.
     */
    private boolean containsSearchTerm(ProductEntity product, String searchTerm) {
        return (product.getTitle() != null &&
                product.getTitle().toLowerCase().contains(searchTerm)) ||
                (product.getDescription() != null &&
                        product.getDescription().toLowerCase().contains(searchTerm)) ||
                (product.getShortDescription() != null &&
                        product.getShortDescription().toLowerCase().contains(searchTerm));
    }

    /**
     * Validates if data has been loaded.
     */
    private void ensureDataLoaded() {
        if (!dataLoaded) {
            throw new DataLoadException("Product data not loaded");
        }
    }

    /**
     * Validates the product ID.
     */
    private void validateProductId(String productId) {
        validateNonBlankString(productId, "Product ID");
    }

    /**
     * Validates if the string is not null or empty.
     */
    private void validateNonBlankString(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Validates the price range.
     */
    private void validatePriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range values cannot be null");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price values cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
    }

    /**
     * Reloads data from file (useful for testing or manual updates).
     */
    public void reloadData() {
        logger.info("Reloading products data");
        productsCache.clear();
        dataLoaded = false;
        loadData();
    }

    /**
     * Returns the number of products in cache.
     */
    public int getCacheSize() {
        return productsCache.size();
    }
}