package com.mercadoclone.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductEntity {

    private String id;
    private String title;
    private String description;

    @JsonProperty("shortDescription")
    private String shortDescription;

    private PriceEntity price;
    private List<ProductImageEntity> images;
    private CategoryEntity category;
    private String brand;
    private String sku;
    private StockEntity stock;
    private ProductRatingEntity rating;

    @JsonProperty("paymentMethods")
    private List<PaymentMethodEntity> paymentMethods;

    private ShippingEntity shipping;
    private SellerEntity seller;
    private List<String> features;
    private Map<String, String> specifications;
    private String warranty;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public ProductEntity() {}

    public ProductEntity(String id, String title, String description) {
        this.id = Objects.requireNonNull(id, "Product ID cannot be null");
        this.title = Objects.requireNonNull(title, "Product title cannot be null");
        this.description = Objects.requireNonNull(description, "Product description cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public PriceEntity getPrice() {
        return price;
    }

    public void setPrice(PriceEntity price) {
        this.price = price;
    }

    public List<ProductImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ProductImageEntity> images) {
        this.images = images;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public StockEntity getStock() {
        return stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public ProductRatingEntity getRating() {
        return rating;
    }

    public void setRating(ProductRatingEntity rating) {
        this.rating = rating;
    }

    public List<PaymentMethodEntity> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodEntity> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public ShippingEntity getShipping() {
        return shipping;
    }

    public void setShipping(ShippingEntity shipping) {
        this.shipping = shipping;
    }

    public SellerEntity getSeller() {
        return seller;
    }

    public void setSeller(SellerEntity seller) {
        this.seller = seller;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAvailable() {
        return stock != null && stock.isAvailable();
    }

    public boolean hasDiscount() {
        return price != null && price.getDiscount() != null && price.getDiscount() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity product = (ProductEntity) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}