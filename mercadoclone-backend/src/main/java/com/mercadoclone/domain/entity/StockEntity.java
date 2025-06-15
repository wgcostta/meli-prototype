package com.mercadoclone.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockEntity(
        @JsonProperty("available")
        Integer available,
        Integer total) {

    public boolean isAvailable() {
        return available != null && available > 0;
    }
}