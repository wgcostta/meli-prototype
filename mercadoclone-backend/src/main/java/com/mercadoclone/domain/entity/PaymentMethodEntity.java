package com.mercadoclone.domain.entity;


public record PaymentMethodEntity(
        String type,
        String name,
        String icon,
        Integer installments,
        Double discount
) {
    /**
     * Check if the payment method allows installments.
     */
    public boolean allowsInstallments() {
        return installments != null && installments > 1;
    }
}