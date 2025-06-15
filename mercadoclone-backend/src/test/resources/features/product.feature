Feature: BDD - Cenários de Consulta de Produtos
  Como um usuário da API
  Eu quero buscar produtos
  Para obter informações detalhadas

  Background:
    Given que a API está rodando
    And existem produtos carregados no sistema

  Scenario: Buscar produto existente
    Given que existe um produto com ID "c71ea0ea-f273-4d2c-8a0e-7afe89294b9a"
    When eu faço uma requisição GET para "/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a"
    Then o status da resposta deve ser 200
    And a resposta deve conter os detalhes do produto
    And o campo "success" deve ser true
    And o campo "data.id" deve ser "c71ea0ea-f273-4d2c-8a0e-7afe89294b9a"

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
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "categoryId" igual a "cat-electronics"
    Then o status da resposta deve ser 200
    And a resposta deve conter produtos da categoria especificada
    And o campo "success" deve ser true

  Scenario: Pesquisar produtos por termo
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "value" igual a "Samsung"
    Then o status da resposta deve ser 200
    And a resposta deve conter produtos relacionados ao termo de busca
    And o campo "success" deve ser true

  Scenario: Filtrar produtos por marca
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "brandId" igual a "Samsung"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a resposta deve conter produtos da marca especificada

  Scenario: Filtrar produtos disponíveis
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "available" igual a "true"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a resposta deve conter apenas produtos disponíveis

  Scenario: Filtrar produtos com desconto
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "discounted" igual a "true"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a resposta deve conter apenas produtos com desconto

  Scenario: Filtrar produtos por faixa de preço válida
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "100" e max "1000"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a resposta deve conter produtos na faixa de preço especificada

  Scenario: Erro ao filtrar com faixa de preço inválida
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "1000" e max "100"
    Then o status da resposta deve ser 400
    And o campo "code" deve ser "INVALID_ARGUMENT"
    And o campo "message" deve conter "Minimum price cannot be greater than maximum price"

  Scenario: Erro ao filtrar com preços negativos
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "-10" e max "100"
    Then o status da resposta deve ser 400
    And o campo "code" deve ser "INVALID_ARGUMENT"
    And o campo "message" deve conter "Price values cannot be negative"

  Scenario: Verificar se produto existe
    When eu faço uma requisição GET para "/api/v1/products/c71ea0ea-f273-4d2c-8a0e-7afe89294b9a/exists"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And o campo "data" deve ser true

  Scenario: Verificar produto inexistente
    When eu faço uma requisição GET para "/api/v1/products/non-existent-product-id/exists"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And o campo "data" deve ser false

  Scenario: Obter contagem total de produtos
    When eu faço uma requisição GET para "/api/v1/products/count"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And o campo "data" deve ser um número maior que zero

  Scenario: Pesquisar com caracteres especiais
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "value" igual a "iphone@#$%"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true

  Scenario: Pesquisar com termo muito longo
    When eu faço uma requisição GET para "/api/v1/products" com termo de busca muito longo
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a lista de produtos deve estar vazia

  Scenario: Filtrar por marca inexistente
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "brandId" igual a "NonExistentBrandXYZ"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    And a lista de produtos deve estar vazia

  Scenario: Verificar cabeçalhos CORS
    When eu faço uma requisição GET para "/api/v1/products" com header Origin
    Then o status da resposta deve ser 200
    And a resposta deve conter cabeçalho "Access-Control-Allow-Origin"

  Scenario: Verificar health check
    When eu faço uma requisição GET para "/actuator/health"
    Then o status da resposta deve ser 200
    And o campo "status" deve ser "UP"

  Scenario: Verificar consistência entre contagem e lista
    When eu obtenho a contagem total de produtos
    And eu obtenho a lista completa de produtos
    Then a contagem deve ser igual ao tamanho da lista

  Scenario: Verificar performance da API
    When eu faço uma requisição GET para "/api/v1/products"
    Then o status da resposta deve ser 200
    And a resposta deve ser retornada em menos de 5 segundos