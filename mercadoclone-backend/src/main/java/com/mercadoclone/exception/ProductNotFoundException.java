/**
 * Exceção lançada quando um produto não é encontrado.
 */
package com.mercadoclone.exception;

public class ProductNotFoundException extends RuntimeException {

    private final String productId;

    public ProductNotFoundException(String message) {
        super(message);
        this.productId = null;
    }

    public ProductNotFoundException(String message, String productId) {
        super(message);
        this.productId = productId;
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.productId = null;
    }

    public String getProductId() {
        return productId;
    }
}
