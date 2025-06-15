package com.mercadoclone.dto.response;


public record PriceResponse(
        Double current,
        Double original,
        String currency,
        Integer discount
) {
}