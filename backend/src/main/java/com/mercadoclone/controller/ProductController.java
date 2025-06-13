package com.mercadoclone.controller;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.response.ApiResponse;
import com.mercadoclone.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
    public ResponseEntity<ApiResponse<ProductEntity>> getProductById(
            @Parameter(description = "ID único do produto", required = true)
            @PathVariable @NotBlank String productId) {

        logger.info("REST request to get product by ID: {}", productId);

        ProductEntity product = productService.getProductById(productId);

        logger.debug("Successfully retrieved product: {}", product.getId());
        return ResponseEntity.ok(ApiResponse.success(product));
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
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getAllProducts() {
        logger.info("REST request to get all products");

        List<ProductEntity> products = productService.getAllProducts();

        logger.debug("Successfully retrieved {} products", products.size());
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Buscar produtos por categoria",
            description = "Retorna uma lista de produtos filtrados por categoria"
    )
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getProductsByCategory(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable @NotBlank String categoryId) {

        logger.info("REST request to get products by category: {}", categoryId);

        List<ProductEntity> products = productService.getProductsByCategory(categoryId);

        logger.debug("Successfully retrieved {} products for category: {}", products.size(), categoryId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Buscar produtos por marca",
            description = "Retorna uma lista de produtos filtrados por marca"
    )
    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getProductsByBrand(
            @Parameter(description = "Nome da marca", required = true)
            @PathVariable @NotBlank String brand) {

        logger.info("REST request to get products by brand: {}", brand);

        List<ProductEntity> products = productService.getProductsByBrand(brand);

        logger.debug("Successfully retrieved {} products for brand: {}", products.size(), brand);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Pesquisar produtos",
            description = "Busca produtos por termo de pesquisa no título ou descrição"
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> searchProducts(
            @Parameter(description = "Termo de pesquisa", required = true)
            @RequestParam @NotBlank String q) {

        logger.info("REST request to search products with term: {}", q);

        List<ProductEntity> products = productService.searchProducts(q);

        logger.debug("Successfully found {} products for search term: {}", products.size(), q);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Listar produtos disponíveis",
            description = "Retorna uma lista de produtos com estoque disponível"
    )
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getAvailableProducts() {
        logger.info("REST request to get available products");

        List<ProductEntity> products = productService.getAvailableProducts();

        logger.debug("Successfully retrieved {} available products", products.size());
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Listar produtos com desconto",
            description = "Retorna uma lista de produtos que possuem desconto aplicado"
    )
    @GetMapping("/discounted")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getProductsWithDiscount() {
        logger.info("REST request to get products with discount");

        List<ProductEntity> products = productService.getProductsWithDiscount();

        logger.debug("Successfully retrieved {} products with discount", products.size());
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(
            summary = "Buscar produtos por faixa de preço",
            description = "Retorna uma lista de produtos filtrados por faixa de preço"
    )
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getProductsByPriceRange(
            @Parameter(description = "Preço mínimo", required = true)
            @RequestParam @PositiveOrZero Double minPrice,
            @Parameter(description = "Preço máximo", required = true)
            @RequestParam @PositiveOrZero Double maxPrice) {

        logger.info("REST request to get products by price range: {} - {}", minPrice, maxPrice);

        List<ProductEntity> products = productService.getProductsByPriceRange(minPrice, maxPrice);

        logger.debug("Successfully retrieved {} products in price range: {} - {}",
                products.size(), minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products));
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