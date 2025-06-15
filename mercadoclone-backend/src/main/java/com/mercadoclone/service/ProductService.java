package com.mercadoclone.service;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.exception.ProductNotFoundException;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço de produtos.
 *
 * Esta implementação segue os princípios SOLID:
 * - SRP: Responsável apenas pela lógica de negócio de produtos
 * - OCP: Pode ser estendida sem modificação
 * - LSP: Substitui perfeitamente a interface ProductService
 * - ISP: Implementa apenas a interface necessária
 * - DIP: Depende de abstrações (ProductRepository)
 *
 * @author MercadoClone Team
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

    @Override
    public List<ProductEntity> findAll(String categoryId, String brandId, String value, Boolean available, Boolean discounted, Double minPrice, Double maxPrice, Boolean rangePrice) {
        logger.info("Getting all products");

        if(StringUtils.isNotBlank(categoryId))
            return getProductsByCategory(categoryId);

        if(StringUtils.isNotBlank(brandId))
            return getProductsByBrand(brandId);

        if(StringUtils.isNotBlank(value))
            return searchProducts(value);

        if(available != null && available)
            return getAvailableProducts();

        if(discounted != null && discounted)
            return getProductsWithDiscount();

        if(rangePrice != null && rangePrice )
            return getProductsByPriceRange(minPrice, maxPrice);

        List<ProductEntity> products = productRepository.findAll();

        logger.debug("Found {} products", products.size());

        return products;
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
     * Valida o ID do produto.
     */
    private void validateProductId(String productId) {
        validateNonBlankString(productId, "Product ID");
    }

    /**
     * Valida se a string não é nula ou vazia.
     */
    private void validateNonBlankString(String value, String fieldName) {
        if (!org.springframework.util.StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Valida a faixa de preços.
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