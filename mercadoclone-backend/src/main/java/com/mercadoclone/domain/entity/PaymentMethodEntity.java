package com.mercadoclone.domain.entity;


/**
 * Entidade que representa um método de pagamento.
 */
public record PaymentMethodEntity(
        String type,
        String name,
        String icon,
        Integer installments,
        Double discount
) {
    /**
     * Verifica se o método de pagamento permite parcelamento.
     */
    public boolean allowsInstallments() {
        return installments != null && installments > 1;
    }
}