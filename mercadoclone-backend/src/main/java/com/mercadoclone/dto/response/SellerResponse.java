package com.mercadoclone.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record  SellerResponse(
        String id,
        String name,
        Double reputation,
        String location,
        @JsonProperty("isOfficial") Boolean isOfficial,
        @JsonProperty("positiveRating") Integer positiveRating,
        @JsonProperty("yearsOnPlatform") Integer yearsOnPlatform,
        String avatar
) {

}