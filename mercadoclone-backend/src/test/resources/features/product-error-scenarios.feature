Feature: BDD - Cenários de Erro da API de Produtos
  Como um usuário da API
  Eu quero que a API trate erros adequadamente
  Para ter uma experiência consistente e previsível

  Background:
    Given que a API está rodando

  Scenario: Erro 404 para produto não encontrado
    When eu faço uma requisição GET para "/api/v1/products/non-existent-id"
    Then o status da resposta deve ser 404
    And o campo "code" deve ser "PRODUCT_NOT_FOUND"
    And o campo "message" deve conter "Product not found"

  Scenario: Erro 400 para faixa de preço inválida - mínimo maior que máximo
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "1000" e max "100"
    Then o status da resposta deve ser 400
    And o campo "code" deve ser "INVALID_ARGUMENT"
    And o campo "message" deve conter "Minimum price cannot be greater than maximum price"

  Scenario: Erro 400 para valores de preço negativos
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "-10" e max "100"
    Then o status da resposta deve ser 400
    And o campo "code" deve ser "INVALID_ARGUMENT"
    And o campo "message" deve conter "Price values cannot be negative"

  Scenario: Erro 400 para valores de preço nulos quando rangePrice está habilitado
    When eu faço uma requisição GET para "/api/v1/products?rangePrice=true"
    Then o status da resposta deve ser 400
    And o campo "code" deve ser "INVALID_ARGUMENT"
    And o campo "message" deve conter "Price range values cannot be null"

  # Corrigido: O endpoint /exists retorna 404, não 500
  Scenario: Erro 404 para produto não encontrado no endpoint exists
    When eu faço uma requisição GET para "/api/v1/products/exists"
    Then o status da resposta deve ser 404
    And o campo "code" deve ser "PRODUCT_NOT_FOUND"

  Scenario Outline: Validação de parâmetros com valores vazios
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "<param>" igual a ""
    Then o status da resposta deve ser <status>
    And o campo "success" deve ser <success>

    Examples:
      | param      | status | success |
      | brandId    | 200    | true    |
      | categoryId | 200    | true    |
      | value      | 200    | true    |

  Scenario: Validação de parâmetro available com valor inválido
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "available" igual a "invalid"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true

  Scenario: Validação de parâmetro discounted com valor inválido
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "discounted" igual a "not_boolean"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true

  Scenario: Tratamento gracioso de múltiplos filtros
    When eu faço uma requisição GET para "/api/v1/products?categoryId=cat1&brandId=brand1&available=true"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true
    # Apenas o primeiro filtro (categoryId) deve ser processado

  Scenario: Teste de edge case com faixa de preço zero
    When eu faço uma requisição GET para "/api/v1/products" com parâmetros de preço min "0" e max "0"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true

  Scenario: Pesquisa case insensitive
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "value" igual a "SAMSUNG"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true

  Scenario: Teste de caracteres especiais em filtros
    When eu faço uma requisição GET para "/api/v1/products" com parâmetro "brandId" igual a "Brand@#$%"
    Then o status da resposta deve ser 200
    And o campo "success" deve ser true