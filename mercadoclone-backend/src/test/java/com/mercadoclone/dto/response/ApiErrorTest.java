package com.mercadoclone.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiError Tests")
class ApiErrorTest {

    @Test
    @DisplayName("Should create ApiError with default constructor")
    void shouldCreateApiErrorWithDefaultConstructor() {
        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiError apiError = new ApiError();

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(apiError.getMessage()).isNull();
        assertThat(apiError.getCode()).isNull();
        assertThat(apiError.getDetails()).isNull();
        assertThat(apiError.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should create ApiError with message and code")
    void shouldCreateApiErrorWithMessageAndCode() {
        String message = "Produto não encontrado";
        String code = "PRODUCT_NOT_FOUND";
        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiError apiError = new ApiError(message, code);

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(apiError.getMessage()).isEqualTo(message);
        assertThat(apiError.getCode()).isEqualTo(code);
        assertThat(apiError.getDetails()).isNull();
        assertThat(apiError.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should create ApiError with message, code and details")
    void shouldCreateApiErrorWithMessageCodeAndDetails() {
        String message = "Erro de validação";
        String code = "VALIDATION_ERROR";
        Map<String, Object> details = new HashMap<>();
        details.put("field", "email");
        details.put("rejectedValue", "invalid-email");
        details.put("constraint", "deve ser um email válido");

        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiError apiError = new ApiError(message, code, details);

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(apiError.getMessage()).isEqualTo(message);
        assertThat(apiError.getCode()).isEqualTo(code);
        assertThat(apiError.getDetails()).isEqualTo(details);
        assertThat(apiError.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        ApiError apiError = new ApiError();
        LocalDateTime customTimestamp = LocalDateTime.of(2024, 6, 15, 10, 30, 45);
        Map<String, Object> details = new HashMap<>();
        details.put("errorId", "ERR123");
        details.put("severity", "HIGH");

        apiError.setMessage("Erro interno do servidor");
        apiError.setCode("INTERNAL_SERVER_ERROR");
        apiError.setDetails(details);
        apiError.setTimestamp(customTimestamp);

        assertThat(apiError.getMessage()).isEqualTo("Erro interno do servidor");
        assertThat(apiError.getCode()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(apiError.getDetails()).isEqualTo(details);
        assertThat(apiError.getTimestamp()).isEqualTo(customTimestamp);
    }

    @Test
    @DisplayName("Should set timestamp automatically in default constructor")
    void shouldSetTimestampAutomaticallyInDefaultConstructor() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ApiError apiError = new ApiError();

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(apiError.getTimestamp()).isAfter(before);
        assertThat(apiError.getTimestamp()).isBefore(after);
    }

    @Test
    @DisplayName("Should preserve timestamp when created with parameterized constructor")
    void shouldPreserveTimestampWhenCreatedWithParameterizedConstructor() {

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ApiError apiError = new ApiError("Erro", "CODE");

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(apiError.getTimestamp()).isAfter(before);
        assertThat(apiError.getTimestamp()).isBefore(after);
    }
}