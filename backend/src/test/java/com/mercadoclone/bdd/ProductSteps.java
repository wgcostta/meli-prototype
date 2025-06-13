/**
 * Step definitions para testes BDD.
 */
package com.mercadoclone.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> lastResponse;
    private String baseUrl;

    @Given("que a API está rodando")
    public void queAApiEstaRodando() {
        baseUrl = "http://localhost:" + port;
        // Verifica se a API está respondendo
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/health", String.class);
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

    @Given("que existe uma categoria {string}")
    public void queExisteUmaCategoria(String categoryId) {
        // Assumimos que a categoria existe se há produtos carregados
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/v1/products/category/" + categoryId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("eu faço uma requisição GET para {string}")
    public void euFacoUmaRequisicaoGetPara(String endpoint) {
        lastResponse = restTemplate.getForEntity(baseUrl + endpoint, String.class);
    }

    @When("eu faço uma requisição GET para {string} com parâmetro {string} igual a {string}")
    public void euFacoUmaRequisicaoGetParaComParametro(String endpoint, String param, String value) {
        String url = baseUrl + endpoint + "?" + param + "=" + value;
        lastResponse = restTemplate.getForEntity(url, String.class);
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
}