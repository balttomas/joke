package com.laughter.joke.mediator;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.laughter.joke.base.Joke;
import feign.FeignException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
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
class JokeMediatorIntegrationTest {

  private static final String RANDOM_JOKE_URL = "/random";
  private static final String SEARCH_JOKES_URL = "/search";
  private static final int INVOCATIONS_AMOUNT = 5;

  @Autowired
  private JokeMediatorApi jokeMediator;

  @Test
  void shouldPropagate5XXToUpperLayer() {
    stubFor(get(urlEqualTo(RANDOM_JOKE_URL))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
        ));

    assertThrows(FeignException.ServiceUnavailable.class, () -> jokeMediator.findRandomJoke());

    verify(exactly(1), getRequestedFor(urlEqualTo(RANDOM_JOKE_URL)));
  }

  @Test
  void shouldLimitRetrievalOfRandomJokeByProvidingFallbackResult() throws IOException {
    stubFor(get(urlEqualTo(RANDOM_JOKE_URL))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(parseStub("stubs/successJoke.json"))
        ));

    List<Joke> result = Stream.generate(jokeMediator::findRandomJoke).limit(INVOCATIONS_AMOUNT)
        .toList();

    assertThat(result)
        .isNotNull()
        .hasSize(INVOCATIONS_AMOUNT);
    assertThat(result.stream().map(Joke::getJoke))
        .contains(
            "You're so eager for huge amount of jokes. Be patient. Wait a second and try receiving some again.",
            "Chuck Norris went on a hike up mount...");
  }

  @Test
  void shouldLimitRetrievingJokesByCategoryByProvidingFallbackResult() throws IOException {
    String categoryName = "categoryName2";
    stubFor(get(urlPathMatching(RANDOM_JOKE_URL))
        .withQueryParam("category", equalTo(categoryName))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(parseStub("stubs/successJokeByCategory.json"))
        ));

    List<Joke> result = Stream.generate(() -> jokeMediator.findRandomJokeByCategory(categoryName))
        .limit(INVOCATIONS_AMOUNT).toList();

    assertThat(result)
        .isNotNull()
        .hasSize(INVOCATIONS_AMOUNT);
    assertThat(result.stream().map(Joke::getJoke))
        .contains(
            String.format(
                "Jokes of type %s are so awesome. Be patient. Wait a second and try receiving some again.",
                categoryName),
            "Chuck Norris doesn't do Burn Down charts, he does Smack Down charts.");
  }

  @Test
  void shouldLimitSearchingForJokesByProvidingFallbackResult() throws IOException {
    String queryValue = "season";
    stubFor(get(urlPathMatching(SEARCH_JOKES_URL))
        .withQueryParam("query", equalTo(queryValue))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(parseStub("stubs/successSearchForJokes.json"))
        ));

    List<Joke> result = Stream.generate(() -> jokeMediator.findManyJokes(queryValue))
        .limit(INVOCATIONS_AMOUNT).flatMap(Collection::stream).toList();

    assertThat(result)
        .isNotNull();
    assertThat(result.stream().map(Joke::getJoke))
        .contains(
            "Sadaharu Oh holds the world record with 868 home runs in his career. Chuck Norris could break that record in a single season.",
            "Like many other magnificient creatures, Chuck Norris has his own mating season, we know this season better as winter",
            "Your search for jokes by criteria season is busy a bit. Be patient. Wait a second and try receiving some again."
        );
  }

  private String parseStub(String location) throws IOException {
    return IOUtils.toString(new ClassPathResource(location).getInputStream(), UTF_8);
  }
}
