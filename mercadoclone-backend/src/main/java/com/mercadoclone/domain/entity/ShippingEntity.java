package com.mercadoclone.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingEntity {
    private Boolean free;

    @JsonProperty("estimatedDays")
    private Integer estimatedDays;

    private Double cost;
    private String description;

    public ShippingEntity() {}

    public ShippingEntity(Boolean free, Integer estimatedDays, Double cost, String description) {
        this.free = free;
        this.estimatedDays = estimatedDays;
        this.cost = cost;
        this.description = description;
    }

    public Boolean getFree() { return free; }
    public void setFree(Boolean free) { this.free = free; }

    public Integer getEstimatedDays() { return estimatedDays; }
    public void setEstimatedDays(Integer estimatedDays) { this.estimatedDays = estimatedDays; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isExpress() {
        return estimatedDays != null && estimatedDays <= 2;
    }
}