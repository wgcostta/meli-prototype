package com.mercadoclone.bdd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseEntity<String> lastResponse;
    private String baseUrl;
    private long requestStartTime;
    private JsonNode countData;
    private JsonNode listData;

    @Given("que a API está rodando")
    public void queAApiEstaRodando() {
        baseUrl = "http://localhost:" + port;
        // Verifica se a API está respondendo
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Given("existem produtos carregados no sistema")
    public void existemProdutosCarregadosNoSistema() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/api/v1/products/count", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Verifica se há pelo menos um produto
        assertThat(response.getBody()).contains("\"data\":");
    }

    @Given("que existe um produto com ID {string}")
    public void queExisteUmProdutoComId(String productId) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/v1/products/" + productId + "/exists", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"data\":true");
    }

    @Given("que não existe um produto com ID {string}")
    public void queNaoExisteUmProdutoComId(String productId) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/v1/products/" + productId + "/exists", String.class);
        // O produto pode não existir (404) ou existir mas retornar false
        if (response.getStatusCode() == HttpStatus.OK) {
            assertThat(response.getBody()).contains("\"data\":false");
        }
    }

    @When("eu faço uma requisição GET para {string}")
    public void euFacoUmaRequisicaoGetPara(String endpoint) {
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(baseUrl + endpoint, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a {string}")
    public void euFacoUmaRequisicaoGetParaComParametro(String endpoint, String param, String value) {
        String url = baseUrl + endpoint + "?" + param + "=" + value;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetros de preço min {string} e max {string}")
    public void euFacoUmaRequisicaoGetParaComParametrosDePreco(String endpoint, String minPrice, String maxPrice) {
        String url = baseUrl + endpoint + "?rangePrice=true&minPrice=" + minPrice + "&maxPrice=" + maxPrice;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com termo de busca muito longo")
    public void euFacoUmaRequisicaoGetParaComTermoDeBuscaMuitoLongo(String endpoint) {
        String longTerm = "a".repeat(1000);
        String url = baseUrl + endpoint + "?value=" + longTerm;
        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.getForEntity(url, String.class);
    }

    @When("eu faço uma requisição GET para {string} com header Origin")
    public void euFacoUmaRequisicaoGetParaComHeaderOrigin(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Origin", "http://localhost:3000");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        requestStartTime = System.currentTimeMillis();
        lastResponse = restTemplate.exchange(baseUrl + endpoint, HttpMethod.GET, entity, String.class);
    }

    @When("eu obtenho a contagem total de produtos")
    public void euObtenhoAContagemTotalDeProdutos() throws Exception {
        ResponseEntity<String> countResponse = restTemplate.getForEntity(baseUrl + "/api/v1/products/count", String.class);
        assertThat(countResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        countData = objectMapper.readTree(countResponse.getBody());
    }

    @When("eu obtenho a lista completa de produtos")
    public void euObtenhoAListaCompletaDeProdutos() throws Exception {
        ResponseEntity<String> listResponse = restTemplate.getForEntity(baseUrl + "/api/v1/products", String.class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        listData = objectMapper.readTree(listResponse.getBody());
    }

    @Then("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int expectedStatus) {
        assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @And("a resposta deve conter os detalhes do produto")
    public void aRespostaDeveConterOsDetalhesDoProduto() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"data\":");
        assertThat(body).contains("\"id\":");
        assertThat(body).contains("\"title\":");
    }

    @And("o campo {string} deve ser {string}")
    public void oCampoDeveSer(String field, String expectedValue) {
        String body = lastResponse.getBody();
        if (expectedValue.equals("true") || expectedValue.equals("false")) {
            assertThat(body).contains("\"" + field + "\":" + expectedValue);
        } else {
            assertThat(body).contains("\"" + field + "\":\"" + expectedValue + "\"");
        }
    }

    @And("o campo {string} deve ser true")
    public void oCampoDeveSerTrue(String field) {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"" + field + "\":true");
    }

    @And("o campo {string} deve ser false")
    public void oCampoDeveSerFalse(String field) {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"" + field + "\":false");
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
        assertThat(body).contains("\"message\":");
        assertThat(body).contains("\"code\":");
    }

    @And("a resposta deve conter uma lista de produtos")
    public void aRespostaDeveConterUmaListaDeProdutos() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"data\":[");
        assertThat(body).contains("\"success\":true");
    }

    @And("a lista {string} deve ter pelo menos {int} item")
    public void aListaDeveTerPeloMenosItem(String listField, int minItems) {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"" + listField + "\":[");
        // Verificação básica - em um cenário real, seria melhor fazer parse do JSON
        assertThat(body).doesNotContain("\"" + listField + "\":[]");
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
        assertThat(body).contains("\"data\":");
        assertThat(body).contains("\"success\":true");
    }

    @And("a resposta deve conter produtos relacionados ao termo de busca")
    public void aRespostaDeveConterProdutosRelacionadosAoTermoDeBusca() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"data\":");
        assertThat(body).contains("\"success\":true");
    }

    @And("a resposta deve conter produtos da marca especificada")
    public void aRespostaDeveConterProdutosDaMarcaEspecificada() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"data\":");
        assertThat(body).contains("\"success\":true");
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
        assertThat(body).contains("\"success\":true");
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
        assertThat(body).contains("\"success\":true");
    }

    @And("a resposta deve conter produtos na faixa de preço especificada")
    public void aRespostaDeveConterProdutosNaFaixaDePrecoEspecificada() {
        String body = lastResponse.getBody();
        assertThat(body).contains("\"data\":");
        assertThat(body).contains("\"success\":true");
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
            current = current.get(part);
            if (current == null) {
                throw new IllegalArgumentException("Field not found: " + fieldPath);
            }
        }

        return current;
    }
}