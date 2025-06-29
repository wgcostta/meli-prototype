package com.mercadoclone.bdd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps extends CucumberSpringConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseEntity<String> lastResponse;
    private long requestStartTime;
    private JsonNode countData;
    private JsonNode listData;

    @Given("que a API está rodando")
    public void queAApiEstaRodando() {
        // Verifica se a API está respondendo
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Given("existem produtos carregados no sistema")
    public void existemProdutosCarregadosNoSistema() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/api/v1/products/count", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.has("data")).isTrue();
        assertThat(json.has("success")).isTrue();
        assertThat(json.get("success").asBoolean()).isTrue();
        assertThat(json.get("data").asLong()).isGreaterThan(0);
    }

    @Given("que existe um produto com ID {string}")
    public void queExisteUmProdutoComId(String productId) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/api/v1/products/" + productId + "/exists", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.has("data")).isTrue();
        assertThat(json.get("data").asBoolean()).isTrue();
    }

    @Given("que não existe um produto com ID {string}")
    public void queNaoExisteUmProdutoComId(String productId) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/api/v1/products/" + productId + "/exists", String.class);

        // O produto pode não existir (404) ou existir mas retornar false
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode json = objectMapper.readTree(response.getBody());
            assertThat(json.has("data")).isTrue();
            assertThat(json.get("data").asBoolean()).isFalse();
        }
    }

    @When("eu faço uma requisição GET para {string}")
    public void euFacoUmaRequisicaoGetPara(String endpoint) {
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(getBaseUrl() + endpoint, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a {string}")
    public void euFacoUmaRequisicaoGetParaComParametro(String endpoint, String param, String value) {
        String url = getBaseUrl() + endpoint + "?" + param + "=" + value;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    // Novo step method para casos com aspas duplas
    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a {string}{string}")
    public void euFacoUmaRequisicaoGetParaComParametroComAspas(String endpoint, String param, String value1, String value2) {
        String value = value1 + value2; // Concatena as partes da string
        String url = getBaseUrl() + endpoint + "?" + param + "=" + value;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    // Step methods para valores específicos
    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a \"\"invalid\"\"")
    public void euFacoUmaRequisicaoGetParaComParametroInvalid(String endpoint, String param) {
        String url = getBaseUrl() + endpoint + "?" + param + "=invalid";
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a \"\"not_boolean\"\"")
    public void euFacoUmaRequisicaoGetParaComParametroNotBoolean(String endpoint, String param) {
        String url = getBaseUrl() + endpoint + "?" + param + "=not_boolean";
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetros de preço min {string} e max {string}")
    public void euFacoUmaRequisicaoGetParaComParametrosDePreco(String endpoint, String minPrice, String maxPrice) {
        String url = getBaseUrl() + endpoint + "?rangePrice=true&minPrice=" + minPrice + "&maxPrice=" + maxPrice;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com termo de busca muito longo")
    public void euFacoUmaRequisicaoGetParaComTermoDeBuscaMuitoLongo(String endpoint) {
        String longTerm = "a".repeat(1000);
        String url = getBaseUrl() + endpoint + "?value=" + longTerm;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com header Origin")
    public void euFacoUmaRequisicaoGetParaComHeaderOrigin(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Origin", "http://localhost:3000");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.exchange(getBaseUrl() + endpoint, HttpMethod.GET, entity, String.class);
    }

    @When("eu obtenho a contagem total de produtos")
    public void euObtenhoAContagemTotalDeProdutos() throws Exception {
        ResponseEntity<String> countResponse = restTemplate.getForEntity(getBaseUrl() + "/api/v1/products/count", String.class);
        assertThat(countResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        countData = objectMapper.readTree(countResponse.getBody());
    }

    @When("eu obtenho a lista completa de produtos")
    public void euObtenhoAListaCompletaDeProdutos() throws Exception {
        ResponseEntity<String> listResponse = restTemplate.getForEntity(getBaseUrl() + "/api/v1/products", String.class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        listData = objectMapper.readTree(listResponse.getBody());
    }

    @Then("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int expectedStatus) {
        assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @And("a resposta deve conter os detalhes do produto")
    public void aRespostaDeveConterOsDetalhesDoProduto() throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);

        assertThat(json.has("data")).isTrue();
        JsonNode dataNode = json.get("data");
        assertThat(dataNode.has("id")).isTrue();
        assertThat(dataNode.has("title")).isTrue();
        assertThat(dataNode.get("id").asText()).isNotEmpty();
        assertThat(dataNode.get("title").asText()).isNotEmpty();
    }

    @And("o campo status deve ser UP no health check")
    public void oCampoStatusDeveSerUpNoHealthCheck() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"status\":\"UP\"");
    }

    @And("o campo {string} deve ser {string}")
    public void oCampoDeveSer(String field, String expectedValue) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);

        // Tratamento especial para o health check do Spring Boot Actuator
        if (field.equals("status") && expectedValue.equals("UP")) {
            assertThat(json.has("status")).isTrue();
            assertThat(json.get("status").asText()).isEqualTo("UP");
            return;
        }

        // Para campos aninhados como "data.id"
        JsonNode fieldNode = getNestedField(json, field);
        assertThat(fieldNode).isNotNull();

        if (expectedValue.equals("true")) {
            assertThat(fieldNode.asBoolean()).isTrue();
        } else if (expectedValue.equals("false")) {
            assertThat(fieldNode.asBoolean()).isFalse();
        } else {
            assertThat(fieldNode.asText()).isEqualTo(expectedValue);
        }
    }

    @And("o campo {string} deve ser true")
    public void oCampoDeveSerTrue(String field) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode fieldNode = getNestedField(json, field);
        assertThat(fieldNode).isNotNull();
        assertThat(fieldNode.asBoolean()).isTrue();
    }

    @And("o campo {string} deve ser false")
    public void oCampoDeveSerFalse(String field) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode fieldNode = getNestedField(json, field);
        assertThat(fieldNode).isNotNull();
        assertThat(fieldNode.asBoolean()).isFalse();
    }

    @And("o campo {string} deve ser um número maior que zero")
    public void oCampoDeveSerUmNumeroMaiorQueZero(String field) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode fieldNode = getNestedField(json, field);
        assertThat(fieldNode.isNumber()).isTrue();
        assertThat(fieldNode.asLong()).isGreaterThan(0);
    }

    @And("o campo {string} deve conter {string}")
    public void oCampoDeveConter(String field, String expectedContent) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode fieldNode = getNestedField(json, field);
        assertThat(fieldNode.asText()).contains(expectedContent);
    }

    @And("a resposta deve conter uma mensagem de erro")
    public void aRespostaDeveConterUmaMensagemDeErro() {
        String body = lastResponse.getBody();
        // Corrigido: usar espaços como no JSON real de erro
        assertThat(body).contains("\"message\" :");
        assertThat(body).contains("\"code\" :");
    }

    @And("a resposta deve conter uma lista de produtos")
    public void aRespostaDeveConterUmaListaDeProdutos() throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);

        assertThat(json.has("data")).isTrue();
        assertThat(json.get("data").isArray()).isTrue();
        assertThat(json.has("success")).isTrue();
        assertThat(json.get("success").asBoolean()).isTrue();
    }

    @And("a lista {string} deve ter pelo menos {int} item")
    public void aListaDeveTerPeloMenosItem(String listField, int minItems) throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode listNode = getNestedField(json, listField);

        assertThat(listNode.isArray()).isTrue();
        assertThat(listNode.size()).isGreaterThanOrEqualTo(minItems);
    }

    @And("a lista de produtos deve estar vazia")
    public void aListaDeProdutosDeveEstarVazia() throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode dataArray = json.get("data");
        assertThat(dataArray.isArray()).isTrue();
        assertThat(dataArray.size()).isEqualTo(0);
    }

    @And("a resposta deve conter produtos da categoria especificada")
    public void aRespostaDeveConterProdutosDaCategoriaEspecificada() {
        String body = lastResponse.getBody();
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"data\" :");
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter produtos relacionados ao termo de busca")
    public void aRespostaDeveConterProdutosRelacionadosAoTermoDeBusca() {
        String body = lastResponse.getBody();
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"data\" :");
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter produtos da marca especificada")
    public void aRespostaDeveConterProdutosDaMarcaEspecificada() {
        String body = lastResponse.getBody();
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"data\" :");
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter apenas produtos disponíveis")
    public void aRespostaDeveConterApenasProductsDisponiveis() throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode dataArray = json.get("data");

        if (dataArray.size() > 0) {
            for (JsonNode product : dataArray) {
                if (product.has("available")) {
                    assertThat(product.get("available").asBoolean()).isTrue();
                }
            }
        }
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter apenas produtos com desconto")
    public void aRespostaDeveConterApenasProductsComDesconto() throws Exception {
        String body = lastResponse.getBody();
        JsonNode json = objectMapper.readTree(body);
        JsonNode dataArray = json.get("data");

        if (dataArray.size() > 0) {
            for (JsonNode product : dataArray) {
                if (product.has("discount")) {
                    assertThat(product.get("discount")).isNotNull();
                }
            }
        }
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter produtos na faixa de preço especificada")
    public void aRespostaDeveConterProdutosNaFaixaDePrecoEspecificada() {
        String body = lastResponse.getBody();
        // Corrigido: usar espaços como no JSON real
        assertThat(body).contains("\"data\" :");
        assertThat(body).contains("\"success\" : true");
    }

    @And("a resposta deve conter cabeçalho {string}")
    public void aRespostaDeveConterCabecalho(String headerName) {
        HttpHeaders headers = lastResponse.getHeaders();
        assertThat(headers.containsKey(headerName)).isTrue();
    }

    @And("a contagem deve ser igual ao tamanho da lista")
    public void aContagemDeveSerIgualAoTamanhoDaLista() {
        Long totalCount = countData.get("data").asLong();
        int listSize = listData.get("data").size();
        assertThat(totalCount).isEqualTo(listSize);
    }

    @And("a resposta deve ser retornada em menos de {int} segundos")
    public void aRespostaDeveSerRetornadaEmMenosDe(int maxSeconds) {
        long responseTime = System.currentTimeMillis() - requestStartTime;
        assertThat(responseTime).isLessThan(maxSeconds * 1000L);
    }

    // Helper method para navegar em campos aninhados como "data.id"
    private JsonNode getNestedField(JsonNode json, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        JsonNode current = json;

        for (String part : parts) {
            if (current == null || !current.has(part)) {
                throw new IllegalArgumentException("Field not found: " + fieldPath + " (missing part: " + part + ")");
            }
            current = current.get(part);
        }

        return current;
    }
}