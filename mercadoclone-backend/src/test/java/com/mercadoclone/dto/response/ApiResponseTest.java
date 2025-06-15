package com.mercadoclone.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResponse Tests")

class ApiResponseTest {
    @Test
    @DisplayName("Should create ApiResponse with default constructor")
    void shouldCreateApiResponseWithDefaultConstructor() {
        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiResponse<String> response = new ApiResponse<>();

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(response.isSuccess()).isFalse(); // default boolean value
        assertThat(response.getData()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should create ApiResponse with success and data")
    void shouldCreateApiResponseWithSuccessAndData() {
        String testData = "Dados de teste";
        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiResponse<String> response = new ApiResponse<>(true, testData);

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(testData);
        assertThat(response.getMessage()).isNull();
        assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should create ApiResponse with success, data and message")
    void shouldCreateApiResponseWithSuccessDataAndMessage() {
        String testData = "Dados de teste";
        String message = "Operação realizada com sucesso";
        LocalDateTime beforeCreation = LocalDateTime.now();

        ApiResponse<String> response = new ApiResponse<>(true, testData, message);

        LocalDateTime afterCreation = LocalDateTime.now();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(testData);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        ApiResponse<Integer> response = new ApiResponse<>();
        LocalDateTime customTimestamp = LocalDateTime.of(2024, 6, 15, 14, 20, 30);

        response.setSuccess(true);
        response.setData(42);
        response.setMessage("Número da resposta");
        response.setTimestamp(customTimestamp);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(42);
        assertThat(response.getMessage()).isEqualTo("Número da resposta");
        assertThat(response.getTimestamp()).isEqualTo(customTimestamp);
    }

    @Nested
    @DisplayName("Static Factory Methods Tests")
    class StaticFactoryMethodsTests {

        @Test
        @DisplayName("success(data) - Should create success response with data")
        void shouldCreateSuccessResponseWithData() {
            String testData = "Sucesso";
            LocalDateTime beforeCreation = LocalDateTime.now();

            ApiResponse<String> response = ApiResponse.success(testData);

            LocalDateTime afterCreation = LocalDateTime.now();

            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData()).isEqualTo(testData);
            assertThat(response.getMessage()).isNull();
            assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
        }

        @Test
        @DisplayName("success(data, message) - Should create success response with data and message")
        void shouldCreateSuccessResponseWithDataAndMessage() {
            String testData = "Dados importantes";
            String message = "Operação concluída";
            LocalDateTime beforeCreation = LocalDateTime.now();

            ApiResponse<String> response = ApiResponse.success(testData, message);

            LocalDateTime afterCreation = LocalDateTime.now();

            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData()).isEqualTo(testData);
            assertThat(response.getMessage()).isEqualTo(message);
            assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
        }

        @Test
        @DisplayName("error(message) - Should create error response")
        void shouldCreateErrorResponse() {
            String errorMessage = "Algo deu errado";
            LocalDateTime beforeCreation = LocalDateTime.now();

            ApiResponse<Object> response = ApiResponse.error(errorMessage);

            LocalDateTime afterCreation = LocalDateTime.now();

            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getData()).isNull();
            assertThat(response.getMessage()).isEqualTo(errorMessage);
            assertThat(response.getTimestamp()).isBetween(beforeCreation, afterCreation);
        }

        @Test
        @DisplayName("Should work with different data types")
        void shouldWorkWithDifferentDataTypes() {
            // String
            ApiResponse<String> stringResponse = ApiResponse.success("texto");
            assertThat(stringResponse.getData()).isEqualTo("texto");

            // Integer
            ApiResponse<Integer> intResponse = ApiResponse.success(123);
            assertThat(intResponse.getData()).isEqualTo(123);

            // Custom Object
            Map<String, Object> customData = new HashMap<>();
            customData.put("id", 1);
            customData.put("name", "Produto");

            ApiResponse<Map<String, Object>> mapResponse = ApiResponse.success(customData);
            assertThat(mapResponse.getData()).isEqualTo(customData);

            // Null data
            ApiResponse<String> nullDataResponse = ApiResponse.success(null);
            assertThat(nullDataResponse.getData()).isNull();
            assertThat(nullDataResponse.isSuccess()).isTrue();
        }
    }

    @Test
    @DisplayName("Should maintain generics correctly")
    void shouldMaintainGenericsCorrectly() {
        // Teste com Lista
        java.util.List<String> list = java.util.Arrays.asList("item1", "item2", "item3");
        ApiResponse<java.util.List<String>> listResponse = ApiResponse.success(list);

        assertThat(listResponse.getData()).isEqualTo(list);
        assertThat(listResponse.getData()).hasSize(3);
        assertThat(listResponse.getData()).contains("item1", "item2", "item3");
    }

    @Test
    @DisplayName("Should set timestamp automatically in all constructors")
    void shouldSetTimestampAutomaticallyInAllConstructors() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ApiResponse<String> response1 = new ApiResponse<>();
        ApiResponse<String> response2 = new ApiResponse<>(true, "data");
        ApiResponse<String> response3 = new ApiResponse<>(true, "data", "message");
        ApiResponse<String> response4 = ApiResponse.success("data");
        ApiResponse<String> response5 = ApiResponse.success("data", "message");
        ApiResponse<Object> response6 = ApiResponse.error("error");

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(response1.getTimestamp()).isAfter(before).isBefore(after);
        assertThat(response2.getTimestamp()).isAfter(before).isBefore(after);
        assertThat(response3.getTimestamp()).isAfter(before).isBefore(after);
        assertThat(response4.getTimestamp()).isAfter(before).isBefore(after);
        assertThat(response5.getTimestamp()).isAfter(before).isBefore(after);
        assertThat(response6.getTimestamp()).isAfter(before).isBefore(after);
    }
}