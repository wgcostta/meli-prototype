/**
 * Manipulador global de exceções.
 */
package com.mercadoclone.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manipulador global de exceções para a API.
 *
 * Centraliza o tratamento de exceções seguindo o princípio DRY (Don't Repeat Yourself)
 * e fornece respostas consistentes para diferentes tipos de erro.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata exceções de produto não encontrado.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        logger.warn("Product not found: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));
        if (ex.getProductId() != null) {
            details.put("productId", ex.getProductId());
        }

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError(ex.getMessage(), "PRODUCT_NOT_FOUND", details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata exceções de carregamento de dados.
     */
    @ExceptionHandler(DataLoadException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleDataLoadException(DataLoadException ex, WebRequest request) {
        logger.error("Data load error: {}", ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Internal server error", "DATA_LOAD_ERROR", details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Trata exceções de argumentos inválidos.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Invalid argument: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError(ex.getMessage(), "INVALID_ARGUMENT", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções de validação de argumentos de método.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            details.put(error.getField(), error.getDefaultMessage());
        });

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Validation failed", "VALIDATION_ERROR", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções de violação de constraint.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logger.warn("Constraint violation: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Constraint violation", "CONSTRAINT_VIOLATION", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções de tipo de argumento incorreto.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Type mismatch: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));
        details.put("parameter", ex.getName());
        details.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Invalid parameter type", "TYPE_MISMATCH", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata exceções de método HTTP não suportado.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        logger.warn("Method not supported: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));
        details.put("method", ex.getMethod());
        details.put("supportedMethods", ex.getSupportedMethods());

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("HTTP method not supported", "METHOD_NOT_SUPPORTED", details);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    /**
     * Trata exceções de endpoint não encontrado.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleNoHandlerFound(NoHandlerFoundException ex, WebRequest request) {
        logger.warn("No handler found: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", ex.getRequestURL());
        details.put("method", ex.getHttpMethod());

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Endpoint not found", "ENDPOINT_NOT_FOUND", details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata exceções de conteúdo HTTP não legível.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("HTTP message not readable: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("Malformed JSON request", "MALFORMED_JSON", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata todas as outras exceções não específicas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.mercadoclone.dto.response.ApiError> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        com.mercadoclone.dto.response.ApiError error = new com.mercadoclone.dto.response.ApiError("An unexpected error occurred", "INTERNAL_ERROR", details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}