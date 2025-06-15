package com.mercadoclone.service;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.request.FilterRequest;

import java.util.List;

public interface ProductUseCase {

    /**
     * Finds a product by its unique identifier.
     *
     * @param productId the unique product identifier
     * @return the found product
     * @throws ProductNotFoundException if the product is not found
     * @throws IllegalArgumentException if productId is invalid
     */
    ProductEntity findById(String productId);

    /**
     * Lists all available products.
     *
     * @return list of all products
     */
    List<ProductEntity> findAll(FilterRequest filterRequest);

    /**
     * Finds products by category.
     *
     * @param categoryId the category identifier
     * @return list of products from the category
     * @throws IllegalArgumentException if categoryId is invalid
     */
    List<ProductEntity> getProductsByCategory(String categoryId);

    /**
     * Finds products by brand.
     *
     * @param brand the brand name
     * @return list of products from the brand
     * @throws IllegalArgumentException if brand is invalid
     */
    List<ProductEntity> getProductsByBrand(String brand);

    /**
     * Finds products by search term.
     *
     * @param searchTerm the term to search for
     * @return list of products matching the term
     * @throws IllegalArgumentException if searchTerm is invalid
     */
    List<ProductEntity> searchProducts(String searchTerm);

    /**
     * Lists products available in stock.
     *
     * @return list of available products
     */
    List<ProductEntity> getAvailableProducts();

    /**
     * Lists products with discount.
     *
     * @return list of products with discount
     */
    List<ProductEntity> getProductsWithDiscount();

    /**
     * Finds products by price range.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products in the price range
     * @throws IllegalArgumentException if the price range is invalid
     */
    List<ProductEntity> getProductsByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Checks if a product exists.
     *
     * @param productId the product identifier
     * @return true if the product exists
     */
    boolean productExists(String productId);

    /**
     * Returns the total number of registered products.
     *
     * @return total number of products
     */
    long getTotalProductCount();
}