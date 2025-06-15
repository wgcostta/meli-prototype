package com.mercadoclone.dto.mapper;

import com.mercadoclone.domain.entity.*;
import com.mercadoclone.dto.response.PaymentMethodResponse;
import com.mercadoclone.dto.response.ProductImageResponse;
import com.mercadoclone.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper Tests")
class ProductMapperTest {

    private ProductMapper productMapper;
    private ProductEntity productEntity;
    private ProductEntity secondProductEntity;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
        productEntity = createCompleteProductEntity();
        secondProductEntity = createSecondProductEntity();
    }

    @Test
    @DisplayName("Should convert ProductEntity to ProductResponse with all fields")
    void shouldConvertProductEntityToProductResponse() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(productEntity.getId());
        assertThat(response.title()).isEqualTo(productEntity.getTitle());
        assertThat(response.description()).isEqualTo(productEntity.getDescription());
        assertThat(response.shortDescription()).isEqualTo(productEntity.getShortDescription());
        assertThat(response.brand()).isEqualTo(productEntity.getBrand());
        assertThat(response.sku()).isEqualTo(productEntity.getSku());
        assertThat(response.warranty()).isEqualTo(productEntity.getWarranty());
        assertThat(response.features()).isEqualTo(productEntity.getFeatures());
        assertThat(response.specifications()).isEqualTo(productEntity.getSpecifications());
        assertThat(response.createdAt()).isEqualTo(productEntity.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(productEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should convert PriceEntity correctly")
    void shouldConvertPriceEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.price()).isNotNull();
        assertThat(response.price().current()).isEqualTo(productEntity.getPrice().getCurrent());
        assertThat(response.price().original()).isEqualTo(productEntity.getPrice().getOriginal());
        assertThat(response.price().currency()).isEqualTo(productEntity.getPrice().getCurrency());
        assertThat(response.price().discount()).isEqualTo(productEntity.getPrice().getDiscount());
    }

    @Test
    @DisplayName("Should convert CategoryEntity correctly")
    void shouldConvertCategoryEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.category()).isNotNull();
        assertThat(response.category().id()).isEqualTo(productEntity.getCategory().id());
        assertThat(response.category().name()).isEqualTo(productEntity.getCategory().name());
        assertThat(response.category().path()).isEqualTo(productEntity.getCategory().path());
    }

    @Test
    @DisplayName("Should convert StockEntity correctly")
    void shouldConvertStockEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.stock()).isNotNull();
        assertThat(response.stock().available()).isEqualTo(productEntity.getStock().available());
        assertThat(response.stock().total()).isEqualTo(productEntity.getStock().total());
    }

    @Test
    @DisplayName("Should convert ProductRatingEntity correctly")
    void shouldConvertProductRatingEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.rating()).isNotNull();
        assertThat(response.rating().average()).isEqualTo(productEntity.getRating().getAverage());
        assertThat(response.rating().count()).isEqualTo(productEntity.getRating().getCount());
        assertThat(response.rating().distribution()).isEqualTo(productEntity.getRating().getDistribution());
    }

    @Test
    @DisplayName("Should convert ProductImageEntity list correctly")
    void shouldConvertProductImageEntityList() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.images()).isNotNull();
        assertThat(response.images()).hasSize(2);

        ProductImageEntity firstImage = productEntity.getImages().get(0);
        ProductImageResponse responseFirstImage = response.images().get(0);
        assertThat(responseFirstImage.id()).isEqualTo(firstImage.getId());
        assertThat(responseFirstImage.url()).isEqualTo(firstImage.getUrl());
        assertThat(responseFirstImage.alt()).isEqualTo(firstImage.getAlt());
        assertThat(responseFirstImage.order()).isEqualTo(firstImage.getOrder());
    }

    @Test
    @DisplayName("Should convert PaymentMethodEntity list correctly")
    void shouldConvertPaymentMethodEntityList() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.paymentMethods()).isNotNull();
        assertThat(response.paymentMethods()).hasSize(2);

        PaymentMethodEntity firstPayment = productEntity.getPaymentMethods().get(0);
        PaymentMethodResponse responseFirstPayment = response.paymentMethods().get(0);
        assertThat(responseFirstPayment.type()).isEqualTo(firstPayment.type());
        assertThat(responseFirstPayment.name()).isEqualTo(firstPayment.name());
        assertThat(responseFirstPayment.icon()).isEqualTo(firstPayment.icon());
        assertThat(responseFirstPayment.installments()).isEqualTo(firstPayment.installments());
        assertThat(responseFirstPayment.discount()).isEqualTo(firstPayment.discount());
    }

    @Test
    @DisplayName("Should convert ShippingEntity correctly")
    void shouldConvertShippingEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.shipping()).isNotNull();
        assertThat(response.shipping().free()).isEqualTo(productEntity.getShipping().getFree());
        assertThat(response.shipping().estimatedDays()).isEqualTo(productEntity.getShipping().getEstimatedDays());
        assertThat(response.shipping().cost()).isEqualTo(productEntity.getShipping().getCost());
        assertThat(response.shipping().description()).isEqualTo(productEntity.getShipping().getDescription());
        assertFalse(response.shipping().cost().isInfinite());
    }

    @Test
    @DisplayName("Should convert SellerEntity correctly")
    void shouldConvertSellerEntity() {
        // When
        ProductResponse response = productMapper.toResponse(productEntity);

        // Then
        assertThat(response.seller()).isNotNull();
        assertThat(response.seller().id()).isEqualTo(productEntity.getSeller().getId());
        assertThat(response.seller().name()).isEqualTo(productEntity.getSeller().getName());
        assertThat(response.seller().reputation()).isEqualTo(productEntity.getSeller().getReputation());
        assertThat(response.seller().location()).isEqualTo(productEntity.getSeller().getLocation());
        assertThat(response.seller().isOfficial()).isEqualTo(productEntity.getSeller().getIsOfficial());
        assertThat(response.seller().positiveRating()).isEqualTo(productEntity.getSeller().getPositiveRating());
        assertThat(response.seller().yearsOnPlatform()).isEqualTo(productEntity.getSeller().getYearsOnPlatform());
        assertThat(response.seller().avatar()).isEqualTo(productEntity.getSeller().getAvatar());
        assertTrue(response.seller().isOfficial());
    }

    @Test
    @DisplayName("Should handle null ProductEntity")
    void shouldHandleNullProductEntity() {
        // When
        ProductResponse response = productMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should handle ProductEntity with null fields")
    void shouldHandleProductEntityWithNullFields() {
        // Given
        ProductEntity entityWithNulls = new ProductEntity("test-id", "Test Title", "Test Description");

        // When
        ProductResponse response = productMapper.toResponse(entityWithNulls);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("test-id");
        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.description()).isEqualTo("Test Description");
        assertThat(response.price()).isNull();
        assertThat(response.images()).isNull();
        assertThat(response.category()).isNull();
        assertThat(response.stock()).isNull();
        assertThat(response.rating()).isNull();
        assertThat(response.paymentMethods()).isNull();
        assertThat(response.shipping()).isNull();
        assertThat(response.seller()).isNull();
    }

    @Test
    @DisplayName("Should convert list of ProductEntity to list of ProductResponse")
    void shouldConvertProductEntityListToProductResponseList() {
        // Given
        List<ProductEntity> productList = Arrays.asList(productEntity, secondProductEntity);

        // When
        List<ProductResponse> responseList = productMapper.toResponseList(productList);

        // Then
        assertThat(responseList).isNotNull();
        assertThat(responseList).hasSize(2);

        ProductResponse firstResponse = responseList.get(0);
        assertThat(firstResponse.id()).isEqualTo(productEntity.getId());
        assertThat(firstResponse.title()).isEqualTo(productEntity.getTitle());

        ProductResponse secondResponse = responseList.get(1);
        assertThat(secondResponse.id()).isEqualTo(secondProductEntity.getId());
        assertThat(secondResponse.title()).isEqualTo(secondProductEntity.getTitle());
    }

    @Test
    @DisplayName("Should handle empty list")
    void shouldHandleEmptyList() {
        // Given
        List<ProductEntity> emptyList = Collections.emptyList();

        // When
        List<ProductResponse> responseList = productMapper.toResponseList(emptyList);

        // Then
        assertThat(responseList).isNotNull();
        assertThat(responseList).isEmpty();
    }

    @Test
    @DisplayName("Should handle null list")
    void shouldHandleNullList() {
        // When
        List<ProductResponse> responseList = productMapper.toResponseList(null);

        // Then
        assertThat(responseList).isNull();
    }

    @Test
    @DisplayName("Should handle list with null elements")
    void shouldHandleListWithNullElements() {
        // Given
        List<ProductEntity> listWithNulls = Arrays.asList(productEntity, null, secondProductEntity);

        // When
        List<ProductResponse> responseList = productMapper.toResponseList(listWithNulls);

        // Then
        assertThat(responseList).isNotNull();
        assertThat(responseList).hasSize(3);
        assertThat(responseList.get(0)).isNotNull();
        assertThat(responseList.get(1)).isNull();
        assertThat(responseList.get(2)).isNotNull();
    }

    @Test
    @DisplayName("Should verify ProductEntity business methods work correctly")
    void shouldVerifyProductEntityBusinessMethods() {
        // Then
        assertTrue(productEntity.isAvailable());
        assertTrue(productEntity.hasDiscount());

        // Test with no discount
        PriceEntity priceWithoutDiscount = new PriceEntity(99.99, "BRL");
        productEntity.setPrice(priceWithoutDiscount);
        assertFalse(productEntity.hasDiscount());

        // Test with no stock
        productEntity.setStock(new StockEntity(0, 100));
        assertFalse(productEntity.isAvailable());
    }

    // Helper methods to create test data
    private ProductEntity createCompleteProductEntity() {
        ProductEntity product = new ProductEntity("1", "Smartphone Samsung Galaxy", "Latest Samsung smartphone with advanced features");
        product.setShortDescription("Samsung Galaxy - High performance");
        product.setBrand("Samsung");
        product.setSku("SAM-GAL-001");
        product.setWarranty("12 months");
        product.setFeatures(Arrays.asList("5G", "128GB Storage", "Triple Camera"));

        Map<String, String> specs = new HashMap<>();
        specs.put("RAM", "8GB");
        specs.put("Storage", "128GB");
        specs.put("Screen", "6.1 inches");
        product.setSpecifications(specs);

        // Price
        PriceEntity price = new PriceEntity();
        price.setCurrent(899.99);
        price.setOriginal(999.99);
        price.setCurrency("BRL");
        price.setDiscount(10);
        product.setPrice(price);

        // Category
        CategoryEntity category = new CategoryEntity("cat-1", "Electronics", Arrays.asList("Home", "Electronics", "Smartphones"));
        product.setCategory(category);

        // Stock
        StockEntity stock = new StockEntity(50, 100);
        product.setStock(stock);

        // Rating
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("5", 150);
        distribution.put("4", 30);
        distribution.put("3", 10);
        distribution.put("2", 5);
        distribution.put("1", 5);
        ProductRatingEntity rating = new ProductRatingEntity(4.5, 200, distribution);
        product.setRating(rating);

        // Images
        List<ProductImageEntity> images = Arrays.asList(
                new ProductImageEntity("img-1", "https://example.com/image1.jpg", "Front view", 1),
                new ProductImageEntity("img-2", "https://example.com/image2.jpg", "Back view", 2)
        );
        product.setImages(images);

        // Payment Methods
        List<PaymentMethodEntity> paymentMethods = Arrays.asList(
                new PaymentMethodEntity("credit", "Credit Card", "credit-icon", 12, 0.0),
                new PaymentMethodEntity("pix", "PIX", "pix-icon", 1, 5.0)
        );
        product.setPaymentMethods(paymentMethods);

        // Shipping
        ShippingEntity shipping = new ShippingEntity(true, 2, 0.0, "Free express shipping");
        product.setShipping(shipping);

        // Seller
        SellerEntity seller = new SellerEntity("seller-1", "TechStore", "São Paulo, SP");
        seller.setReputation(4.8);
        seller.setIsOfficial(true);
        seller.setPositiveRating(98);
        seller.setYearsOnPlatform(5);
        seller.setAvatar("https://example.com/seller-avatar.jpg");
        product.setSeller(seller);

        // Timestamps
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return product;
    }

    private ProductEntity createSecondProductEntity() {
        ProductEntity product = new ProductEntity("2", "iPhone 15", "Latest Apple iPhone with cutting-edge technology");
        product.setShortDescription("iPhone 15 - Premium experience");
        product.setBrand("Apple");
        product.setSku("APL-IPH-015");

        // Price
        PriceEntity price = new PriceEntity();
        price.setCurrent(4999.99);
        price.setOriginal(5299.99);
        price.setCurrency("BRL");
        price.setDiscount(6);
        product.setPrice(price);

        // Stock
        StockEntity stock = new StockEntity(25, 50);
        product.setStock(stock);

        return product;
    }
}