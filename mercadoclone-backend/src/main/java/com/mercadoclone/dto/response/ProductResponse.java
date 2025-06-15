package com.mercadoclone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ProductResponse (
    String id,
    String title,
    String description,
    String shortDescription,
    PriceResponse price,
    List<ProductImageResponse> images,
    CategoryResponse category,
    String brand,
    String sku,
    StockResponse stock,
    ProductRatingResponse rating,
    List<PaymentMethodResponse> paymentMethods,
    ShippingResponse shipping,
    SellerResponse seller,
    List<String> features,
    Map<String, String> specifications,
    String warranty,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime updatedAt
) {
}