package com.laughter.joke.bdd.definition;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laughter.joke.JokeApplication;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CucumberContextConfiguration
@SpringBootTest(classes = JokeApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class NorrisStepDefinition {

  private HttpResponse<String> response;

  private Scenario scenario;

  private final ObjectMapper mapper = new ObjectMapper();
  private final HttpClient client = HttpClient.newHttpClient();

  @Before
  public void before(Scenario scenario) {
    this.scenario = scenario;
  }

  @When("^client calls GET \"([^\"]+)\"$")
  public void client_calls_jokes_random(final String path)
      throws URISyntaxException, IOException, InterruptedException {
    String url = "http://localhost:" + "8080";
    HttpRequest request = HttpRequest.newBuilder(new URI(url + path))
        .GET()
        .header("Content-Type", "application/json")
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  @Then("^client receives status code (\\d+)")
  public void client_receives_status_code_expected(int statusCode) {
    assertThat(response.statusCode()).isEqualTo(statusCode);
  }

  @And("^response contains joke value")
  public void response_contains_joke_value() {
    assertThat(response).isNotNull();
    assertThat(response.body()).isNotBlank();
  }

  @And("^response contains multiple jokes")
  public void response_contains_multiple_jokes() throws JSONException {
    assertThat(response).isNotNull();
    assertThat(response.body()).isNotBlank();

    JSONArray json = new JSONArray(response.body());
    assertThat(json.length()).isPositive();
  }
}
