package com.mercadoclone.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingResponse (
    @JsonProperty("free")
    Boolean free,
    Integer estimatedDays,
    Double cost,
    String description
) {

}