package com.mercadoclone.service;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.dto.request.FilterRequest;
import com.mercadoclone.exception.ProductNotFoundException;
import com.mercadoclone.service.command.pattern.FilterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Product service implementation.
 *
 * This implementation follows SOLID principles:
 * - SRP: Responsible only for product business logic
 * - OCP: Can be extended without modification
 * - LSP: Perfectly substitutes the ProductService interface
 * - ISP: Implements only the necessary interface
 * - DIP: Depends on abstractions (ProductRepository)
 *
 * @author Wagner Costa
 */
@Service
public class ProductService implements ProductUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity findById(String productId) {
        logger.info("Getting product by ID: {}", productId);

        validateProductId(productId);

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    public List<ProductEntity> findAllWithCommandPattern(FilterRequest filter) {
        FilterCommand command = FilterCommand.fromRequest(filter);

        return command.execute(this, productRepository);
    }

    @Override
    public List<ProductEntity> getProductsByCategory(String categoryId) {
        logger.info("Getting products by category: {}", categoryId);

        validateNonBlankString(categoryId, "Category ID");

        List<ProductEntity> products = productRepository.findByCategory(categoryId);
        logger.debug("Found {} products for category: {}", products.size(), categoryId);

        return products;
    }

    @Override
    public List<ProductEntity> getProductsByBrand(String brand) {
        logger.info("Getting products by brand: {}", brand);

        validateNonBlankString(brand, "Brand");

        List<ProductEntity> products = productRepository.findByBrand(brand);
        logger.debug("Found {} products for brand: {}", products.size(), brand);

        return products;
    }

    @Override
    public List<ProductEntity> searchProducts(String searchTerm) {
        logger.info("Searching products with term: {}", searchTerm);

        validateNonBlankString(searchTerm, "Search term");

        List<ProductEntity> products = productRepository.findBySearchTerm(searchTerm);
        logger.debug("Found {} products for search term: {}", products.size(), searchTerm);

        return products;
    }

    @Override
    public List<ProductEntity> getAvailableProducts() {
        logger.info("Getting available products");

        List<ProductEntity> products = productRepository.findAvailableProducts();
        logger.debug("Found {} available products", products.size());

        return products;
    }

    @Override
    public List<ProductEntity> getProductsWithDiscount() {
        logger.info("Getting products with discount");

        List<ProductEntity> products = productRepository.findProductsWithDiscount();
        logger.debug("Found {} products with discount", products.size());

        return products;
    }

    @Override
    public List<ProductEntity> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        logger.info("Getting products by price range: {} - {}", minPrice, maxPrice);

        validatePriceRange(minPrice, maxPrice);

        List<ProductEntity> products = productRepository.findByPriceRange(minPrice, maxPrice);
        logger.debug("Found {} products in price range: {} - {}", products.size(), minPrice, maxPrice);

        return products;
    }

    @Override
    public boolean productExists(String productId) {
        logger.debug("Checking if product exists: {}", productId);

        validateProductId(productId);

        return productRepository.existsById(productId);
    }

    @Override
    public long getTotalProductCount() {
        logger.debug("Getting total product count");

        long count = productRepository.count();
        logger.debug("Total product count: {}", count);

        return count;
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
        if (!org.springframework.util.StringUtils.hasText(value)) {
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
}