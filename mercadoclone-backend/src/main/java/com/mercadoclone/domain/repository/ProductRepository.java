package com.mercadoclone.domain.repository;

import com.mercadoclone.domain.entity.ProductEntity;
import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório de produtos.
 *
 * Seguindo o princípio de Inversão de Dependência (DIP), esta interface
 * define o contrato para acesso aos dados de produtos sem depender
 * de implementações concretas.
 *
 * @author MercadoClone Team
 */
public interface ProductRepository {

    /**
     * Busca um produto por seu identificador único.
     *
     * @param productId o identificador único do produto
     * @return Optional contendo o produto se encontrado, ou Optional.empty() caso contrário
     * @throws IllegalArgumentException se productId for null ou vazio
     */
    Optional<ProductEntity> findById(String productId);

    /**
     * Busca todos os produtos disponíveis.
     *
     * @return lista de todos os produtos
     */
    List<ProductEntity> findAll();

    /**
     * Busca produtos por categoria.
     *
     * @param categoryId o identificador da categoria
     * @return lista de produtos da categoria especificada
     * @throws IllegalArgumentException se categoryId for null ou vazio
     */
    List<ProductEntity> findByCategory(String categoryId);

    /**
     * Busca produtos por marca.
     *
     * @param brand o nome da marca
     * @return lista de produtos da marca especificada
     * @throws IllegalArgumentException se brand for null ou vazio
     */
    List<ProductEntity> findByBrand(String brand);

    /**
     * Busca produtos por termo de busca no título ou descrição.
     *
     * @param searchTerm o termo a ser buscado
     * @return lista de produtos que contêm o termo de busca
     * @throws IllegalArgumentException se searchTerm for null ou vazio
     */
    List<ProductEntity> findBySearchTerm(String searchTerm);

    /**
     * Busca produtos disponíveis em estoque.
     *
     * @return lista de produtos com estoque disponível
     */
    List<ProductEntity> findAvailableProducts();

    /**
     * Verifica se um produto existe pelo seu identificador.
     *
     * @param productId o identificador único do produto
     * @return true se o produto existir, false caso contrário
     * @throws IllegalArgumentException se productId for null ou vazio
     */
    boolean existsById(String productId);

    /**
     * Conta o número total de produtos.
     *
     * @return o número total de produtos
     */
    long count();

    /**
     * Busca produtos com desconto.
     *
     * @return lista de produtos com desconto aplicado
     */
    List<ProductEntity> findProductsWithDiscount();

    /**
     * Busca produtos por faixa de preço.
     *
     * @param minPrice preço mínimo (inclusive)
     * @param maxPrice preço máximo (inclusive)
     * @return lista de produtos na faixa de preço especificada
     * @throws IllegalArgumentException se minPrice for maior que maxPrice ou se algum for negativo
     */
    List<ProductEntity> findByPriceRange(Double minPrice, Double maxPrice);
}