//package com.mercadoclone.bdd;
//
//import org.junit.platform.suite.api.ConfigurationParameter;
//import org.junit.platform.suite.api.IncludeEngines;
//import org.junit.platform.suite.api.SelectClasspathResource;
//import org.junit.platform.suite.api.Suite;
//import org.junit.platform.suite.api.SuiteDisplayName;
//
//import static io.cucumber.junit.platform.engine.Constants.*;
//
//@Suite
//@SuiteDisplayName("Cucumber BDD Tests")
//@IncludeEngines("cucumber")
//@SelectClasspathResource("features")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.mercadoclone.bdd")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-html-reports, json:target/cucumber-json-reports/Cucumber.json, junit:target/cucumber-junit-reports/Cucumber.xml")
//@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @ignore")
//@ConfigurationParameter(key = EXECUTION_DRY_RUN_PROPERTY_NAME, value = "false")
////@ConfigurationParameter(key = EXECUTION_STRICT_PROPERTY_NAME, value = "true")
//@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
//public class CucumberTestRunner {
//}