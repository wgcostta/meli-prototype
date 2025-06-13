/**
 * Exceção lançada quando há erro no carregamento de dados.
 */
package com.mercadoclone.exception;

public class DataLoadException extends RuntimeException {

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}