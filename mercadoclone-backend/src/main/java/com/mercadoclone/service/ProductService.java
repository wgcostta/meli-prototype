package com.mercadoclone.service;

import com.mercadoclone.domain.entity.ProductEntity;
import java.util.List;

/**
 * Interface do serviço de produtos.
 *
 * Define as operações de negócio relacionadas aos produtos,
 * seguindo o princípio de Segregação de Interface (ISP).
 *
 * @author MercadoClone Team
 */
public interface ProductService {

    /**
     * Busca um produto por seu identificador único.
     *
     * @param productId o identificador único do produto
     * @return o produto encontrado
     * @throws ProductNotFoundException se o produto não for encontrado
     * @throws IllegalArgumentException se productId for inválido
     */
    ProductEntity getProductById(String productId);

    /**
     * Lista todos os produtos disponíveis.
     *
     * @return lista de todos os produtos
     */
    List<ProductEntity> getAllProducts();

    /**
     * Busca produtos por categoria.
     *
     * @param categoryId o identificador da categoria
     * @return lista de produtos da categoria
     * @throws IllegalArgumentException se categoryId for inválido
     */
    List<ProductEntity> getProductsByCategory(String categoryId);

    /**
     * Busca produtos por marca.
     *
     * @param brand o nome da marca
     * @return lista de produtos da marca
     * @throws IllegalArgumentException se brand for inválido
     */
    List<ProductEntity> getProductsByBrand(String brand);

    /**
     * Busca produtos por termo de pesquisa.
     *
     * @param searchTerm o termo a ser pesquisado
     * @return lista de produtos que correspondem ao termo
     * @throws IllegalArgumentException se searchTerm for inválido
     */
    List<ProductEntity> searchProducts(String searchTerm);

    /**
     * Lista produtos disponíveis em estoque.
     *
     * @return lista de produtos disponíveis
     */
    List<ProductEntity> getAvailableProducts();

    /**
     * Lista produtos com desconto.
     *
     * @return lista de produtos com desconto
     */
    List<ProductEntity> getProductsWithDiscount();

    /**
     * Busca produtos por faixa de preço.
     *
     * @param minPrice preço mínimo
     * @param maxPrice preço máximo
     * @return lista de produtos na faixa de preço
     * @throws IllegalArgumentException se a faixa de preço for inválida
     */
    List<ProductEntity> getProductsByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Verifica se um produto existe.
     *
     * @param productId o identificador do produto
     * @return true se o produto existir
     */
    boolean productExists(String productId);

    /**
     * Retorna o total de produtos cadastrados.
     *
     * @return número total de produtos
     */
    long getTotalProductCount();
}
