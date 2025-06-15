package com.mercadoclone.domain.entity;

public class PriceEntity {
    private Double current;
    private Double original;
    private String currency;
    private Integer discount;

    public PriceEntity() {}

    public PriceEntity(Double current, String currency) {
        this.current = current;
        this.currency = currency;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getOriginal() {
        return original;
    }

    public void setOriginal(Double original) {
        this.original = original;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}