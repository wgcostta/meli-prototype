
Feature: Buscar Produto
  Como um usuário da API
  Eu quero buscar produtos
  Para obter informações detalhadas

  Background:
    Given que a API está rodando
    And existem produtos carregados no sistema

  Scenario: Buscar produto existente
    Given que existe um produto com ID "product-001"
    When eu faço uma requisição GET para "/api/v1/products/product-001"
    Then o status da resposta deve ser 200
    And a resposta deve conter os detalhes do produto
    And o campo "success" deve ser true
    And o campo "data.id" deve ser "product-001"

  Scenario: Buscar produto inexistente
    Given que não existe um produto com ID "product-999"
    When eu faço uma requisição GET para "/api/v1/products/product-999"
    Then o status da resposta deve ser 404
    And a resposta deve conter uma mensagem de erro
    And o campo "code" deve ser "PRODUCT_NOT_FOUND"

  Scenario: Listar todos os produtos
    When eu faço uma requisição GET para "/api/v1/products"
    Then o status da resposta deve ser 200
    And a resposta deve conter uma lista de produtos
    And o campo "success" deve ser true
    And a lista "data" deve ter pelo menos 1 item

  Scenario: Buscar produtos por categoria
    Given que existe uma categoria "cat-electronics"
    When eu faço uma requisição GET para "/api/v1/products/category/cat-electronics"
    Then o status da resposta deve ser 200
    And a resposta deve conter produtos da categoria especificada

  Scenario: Pesquisar produtos por termo
    When eu faço uma requisição GET para "/api/v1/products/search" com parâmetro "q" igual a "Samsung"
    Then o status da resposta deve ser 200
    And a resposta deve conter produtos relacionados ao termo de busca

  Scenario: Verificar health check
    When eu faço uma requisição GET para "/health"
    Then o status da resposta deve ser 200
    And o campo "status" deve ser "ok"
