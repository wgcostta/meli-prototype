package com.mercadoclone.dto.response;


public record PaymentMethodResponse(
        String type,
        String name,
        String icon,
        Integer installments,
        Double discount
) {

}