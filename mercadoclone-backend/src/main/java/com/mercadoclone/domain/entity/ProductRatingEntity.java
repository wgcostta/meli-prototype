package com.mercadoclone.domain.entity;

import java.util.Map;

public class ProductRatingEntity {
    private Double average;
    private Integer count;
    private Map<String, Integer> distribution;

    public ProductRatingEntity() {}

    public ProductRatingEntity(Double average, Integer count, Map<String, Integer> distribution) {
        this.average = average;
        this.count = count;
        this.distribution = distribution;
    }

    public Double getAverage() { return average; }
    public void setAverage(Double average) { this.average = average; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public Map<String, Integer> getDistribution() { return distribution; }
    public void setDistribution(Map<String, Integer> distribution) { this.distribution = distribution; }

    public boolean hasGoodRating() {
        return average != null && average >= 4.0;
    }
}