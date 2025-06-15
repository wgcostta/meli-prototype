package com.mercadoclone.dto.response;

import java.util.List;

public record CategoryResponse(
        String id,
        String name,
        List<String> path
) {
}