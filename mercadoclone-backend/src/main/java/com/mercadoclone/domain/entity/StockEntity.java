package com.mercadoclone.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe interna que representa o estoque do produto.
 */
public record StockEntity(
        @JsonProperty("available")
        Integer available,
        @JsonProperty("total")
        Integer total) {

    public boolean isAvailable() {
        return available != null && available > 0;
    }
}