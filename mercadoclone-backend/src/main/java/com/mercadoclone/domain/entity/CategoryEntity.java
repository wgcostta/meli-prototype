package com.mercadoclone.domain.entity;

import java.util.List;

public record CategoryEntity(
        String id,
        String name,
        List<String> path
) {
}