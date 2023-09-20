package com.laughter.joke.bdd.definition;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/norris",
    plugin = {"pretty", "html:target/cucumber/joke.html"},
    extraGlue = "com.laughter.joke.bdd.definition")
public class NorrisCucumberTests {

}
