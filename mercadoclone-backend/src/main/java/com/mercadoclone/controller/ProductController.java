package com.mercadoclone.controller;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.mapper.ProductMapper;
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

/**
 * Controller REST para operações relacionadas a produtos.
 *
 * Seguindo os princípios SOLID:
 * - SRP: Responsável apenas pela camada de apresentação de produtos
 * - OCP: Pode ser estendido sem modificação
 * - DIP: Depende de abstrações (ProductService)
 *
 * @author MercadoClone Team
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "API para gerenciamento de produtos")
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
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes de um produto específico baseado em seu ID único"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "ID do produto inválido"
            )
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(
            @Parameter(description = "ID único do produto", required = true)
            @PathVariable @NotBlank String productId) {

        logger.info("REST request to get product by ID: {}", productId);

        ProductEntity product = productService.findById(productId);

        ProductResponse response = productMapper.toResponse(product);

        logger.debug("Successfully retrieved product: {}", product.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com todos os produtos disponíveis"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(
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
        logger.info("REST request to get all products");

        List<ProductEntity> products = productService.findAll(
                categoryId,
                brandId,
                value,
                available,
                discounted,
                minPrice,
                maxPrice,
                rangePrice
        );

        List<ProductResponse> productResponses = productMapper.toResponseList(products);

        logger.debug("Successfully retrieved {} products", products.size());
        return ResponseEntity.ok(ApiResponse.success(productResponses));
    }

    @Operation(
            summary = "Verificar se produto existe",
            description = "Verifica se um produto existe baseado em seu ID"
    )
    @GetMapping("/{productId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> productExists(
            @Parameter(description = "ID único do produto", required = true)
            @PathVariable @NotBlank String productId) {

        logger.info("REST request to check if product exists: {}", productId);

        boolean exists = productService.productExists(productId);

        logger.debug("Product {} existence check result: {}", productId, exists);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @Operation(
            summary = "Contar total de produtos",
            description = "Retorna o número total de produtos cadastrados"
    )
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalProductCount() {
        logger.info("REST request to get total product count");

        long count = productService.getTotalProductCount();

        logger.debug("Total product count: {}", count);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}