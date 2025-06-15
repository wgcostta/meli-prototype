package com.mercadoclone.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

public record FilterRequest(
        @Parameter(description = "category id")
        @RequestParam(required = false) String categoryId,
        @Parameter(description = "brand id")
        @RequestParam(required = false) String brandId,
        @Parameter(description = "Value from search")
        @RequestParam(required = false) String value,
        @Parameter(description = "Available products")
        @RequestParam (required = false) Boolean available,
        @Parameter(description = "Discounted products")
        @RequestParam(required = false) Boolean discounted,
        @Parameter(description = "Range price")
        @RequestParam(required = false) Boolean rangePrice,
        @Parameter(description = "Minimum price")
        @RequestParam(defaultValue = "0", required = false) Double minPrice,
        @Parameter(description = "Maximum price")
        @RequestParam(defaultValue = "0", required = false) Double maxPrice
) {
}
