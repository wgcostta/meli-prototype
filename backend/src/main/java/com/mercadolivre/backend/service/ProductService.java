package com.mercadolivre.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolivre.backend.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private List<Product> products;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            File file = new File("src/main/resources/products.json");
            if (file.exists()) {
                products = Arrays.asList(objectMapper.readValue(file, Product[].class));
            } else {
                products = List.of();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load products", e);
        }
    }

    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> findAll() {
        return products;
    }
}