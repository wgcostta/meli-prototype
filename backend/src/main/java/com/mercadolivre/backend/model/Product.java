package com.mercadolivre.backend.model;

import java.math.BigDecimal;
import java.util.List;

public class Product {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private List<String> images;
    private List<String> paymentMethods;
    private Seller seller;
    private Integer stock;
    private Double rating;
    private Integer reviewsCount;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public List<String> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(List<String> paymentMethods) { this.paymentMethods = paymentMethods; }
    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(Integer reviewsCount) { this.reviewsCount = reviewsCount; }
}