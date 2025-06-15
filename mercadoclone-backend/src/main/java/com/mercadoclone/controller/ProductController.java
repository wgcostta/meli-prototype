package com.mercadoclone.controller;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.mapper.ProductMapper;
import com.mercadoclone.dto.request.FilterRequest;
import com.mercadoclone.dto.response.ApiResponse;
import com.mercadoclone.dto.response.ProductResponse;
import com.mercadoclone.service.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "API for product management")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductUseCase productService;

    private final ProductMapper productMapper;

    public ProductController(ProductUseCase productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(
            summary = "Find product by ID",
            description = "Returns details of a specific product based on its unique ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product found successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid product ID"
            )
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(
            @Parameter(description = "Unique product ID", required = true)
            @PathVariable @NotBlank String productId) {

        logger.info("REST request to get product by ID: {}", productId);

        ProductEntity product = productService.findById(productId);

        ProductResponse response = productMapper.toResponse(product);

        logger.debug("Successfully retrieved product: {}", product.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "List all products",
            description = "Returns a list with all available products",
            parameters = {
                    @Parameter(name = "categoryId", description = "Category ID to filter products"),
                    @Parameter(name = "brandId", description = "Brand ID to filter products"),
                    @Parameter(name = "value", description = "Search value to filter products"),
                    @Parameter(name = "available", description = "Filter only available products"),
                    @Parameter(name = "discounted", description = "Filter only discounted products"),
                    @Parameter(name = "rangePrice", description = "Filter products by price range"),
                    @Parameter(name = "minPrice", description = "Minimum price for price range filter"),
                    @Parameter(name = "maxPrice", description = "Maximum price for price range filter")
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product list returned successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(
            @Parameter(hidden = true) FilterRequest filterRequest
    ) {
        logger.info("REST request to get all products");

        List<ProductEntity> products = productService.findAllWithCommandPattern(filterRequest);

        List<ProductResponse> productResponses = productMapper.toResponseList(products);

        logger.debug("Successfully retrieved {} products", products.size());

        return ResponseEntity.ok(ApiResponse.success(productResponses));
    }

    @Operation(
            summary = "Check if product exists",
            description = "Verifies if a product exists based on its ID"
    )
    @GetMapping("/{productId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> productExists(
            @Parameter(description = "Unique product ID", required = true)
            @PathVariable @NotBlank String productId) {

        logger.info("REST request to check if product exists: {}", productId);

        boolean exists = productService.productExists(productId);

        logger.debug("Product {} existence check result: {}", productId, exists);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @Operation(
            summary = "Count total products",
            description = "Returns the total number of registered products"
    )
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalProductCount() {
        logger.info("REST request to get total product count");

        long count = productService.getTotalProductCount();

        logger.debug("Total product count: {}", count);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}