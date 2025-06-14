package com.mercadoclone.domain.entity;

import java.util.List;

/**
 * Classe interna que representa a categoria do produto.
 */
public record CategoryEntity(
        String id,
        String name,
        List<String> path
) {
}