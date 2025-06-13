/**
 * Exemplo completo de CucumberTestRunner funcionando.
 */
package com.mercadoclone.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner principal para testes BDD com Cucumber.
 * Esta versão é totalmente compatível e testada.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",           // Localização dos arquivos .feature
        glue = "com.mercadoclone.bdd",            // Pacote com step definitions
        plugin = {
                "pretty",                              // Output formatado no console
                "html:target/cucumber-reports",        // Relatório HTML
                "json:target/cucumber-reports/Cucumber.json",  // Relatório JSON
                "junit:target/cucumber-reports/Cucumber.xml"   // Relatório XML
        },
        tags = "not @ignore",                     // Executa todos exceto @ignore
        dryRun = false                            // false = executa testes reais
)
public class CucumberTestRunner {

}
