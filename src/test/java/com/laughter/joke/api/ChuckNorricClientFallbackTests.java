package com.laughter.joke.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.laughter.joke.client.norris.ChuckNorrisClient;
import feign.FeignException;
import feign.RetryableException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 9561)
class ChuckNorricClientFallbackTests {

  private static final int EXPECTED_RETRIES = 3;
  private static final int NO_RETRIES_EXPECTED = 1;
  private static final String RANDOM_JOKE_URL = "/random";

  @Autowired
  private ChuckNorrisClient chuckNorrisClient;

//  @Disabled("retry mechanism may be applied in upper service layer")
  @Test
  void shouldPropagate5XXToUpperLayer() {
    stubFor(get(urlEqualTo(RANDOM_JOKE_URL))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
        ));

    Assertions.assertThrows(FeignException.ServiceUnavailable.class, () -> chuckNorrisClient.findRandomJoke());

    verify(exactly(NO_RETRIES_EXPECTED), getRequestedFor(urlEqualTo(RANDOM_JOKE_URL)));
  }

  @Test
  void shouldForward4XXExceptionWithoutRetrying() {
    stubFor(get(urlEqualTo(RANDOM_JOKE_URL))
        .willReturn(aResponse()
            .withStatus(HttpStatus.NOT_FOUND.value())
        ));

    Assertions.assertThrows(FeignException.NotFound.class, () -> chuckNorrisClient.findRandomJoke());

    verify(exactly(NO_RETRIES_EXPECTED), getRequestedFor(urlEqualTo(RANDOM_JOKE_URL)));
  }

  @Test
  void shouldReturnJokeAsExpected() throws IOException {
    stubFor(get(urlEqualTo(RANDOM_JOKE_URL))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(parseStub("stubs/successJoke.json"))
        ));

    var result = chuckNorrisClient.findRandomJoke();

    verify(exactly(NO_RETRIES_EXPECTED), getRequestedFor(urlEqualTo(RANDOM_JOKE_URL)));
    assertThat(result).isNotNull();
    assertThat(result.getValue())
        .isNotBlank()
        .isEqualTo("Chuck Norris went on a hike up mount...");
    assertThat(result.getJokeContent()).containsExactly(result.getValue());
  }

  @Disabled("url constructed: /random?category=dev&category=dev&category=dev")
  @Test
  void shouldRetryAndFinallyFailWhen5XXHappens2() {
    stubFor(get(urlEqualTo("/random"))
//        .withQueryParams(Map.of("category", equalTo("dev")))
        .withQueryParam("category", equalTo("dev"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
        ));

    Assertions.assertThrows(RetryableException.class, () -> chuckNorrisClient.findRandomJokeByCategory("dev"));

    verify(exactly(EXPECTED_RETRIES), getRequestedFor(urlEqualTo("/random")).withQueryParam("category", equalTo("dev")));
  }

  private String parseStub(String location) throws IOException {
    return IOUtils.toString(new ClassPathResource(location).getInputStream(), UTF_8);
  }

}
