package com.laughter.joke.mediator;

import com.laughter.joke.base.Joke;
import com.laughter.joke.client.ClientApi;
import com.laughter.joke.exception.JokeNotFoundException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokeMediator implements JokeMediatorApi {

  // TODO to properties with language support
  private static final String NOT_FOUND_BY_QUERY_S = "Not a single joke has been found by query %s";

  @Autowired
  private final ClientApi chuckNorrisClient;

  @Autowired
  private final JokeMediatorApi jokeFallback;

  @Override
  @RateLimiter(name = "norris", fallbackMethod = "findFallbackRandomJoke")
  public Joke findRandomJoke() {
    return Joke.builder().joke(
        Optional.ofNullable(chuckNorrisClient.findRandomJoke())
            .orElseThrow(JokeNotFoundException::new)
            .getJokeContent().get(0)).build();
  }

  @SuppressWarnings("unused")
  private Joke findFallbackRandomJoke(RequestNotPermitted notPermitted) {
    log.warn("API Rate limit has exceeded. Switching to fallback.", notPermitted);
    return jokeFallback.findRandomJoke();
  }

  @Override
  @RateLimiter(name = "norris", fallbackMethod = "findFallbackRandomJokeByCategory")
  public Joke findRandomJokeByCategory(String category) {
    return Joke.builder()
        .joke(
            Optional.ofNullable(chuckNorrisClient.findRandomJokeByCategory(category))
                .orElseThrow(() -> new JokeNotFoundException(
                    String.format("Not a single joke could by found by category - %s", category)))
                .getJokeContent().get(0)).build();
  }

  @SuppressWarnings("unused")
  private Joke findFallbackRandomJokeByCategory(String category,
      RequestNotPermitted notPermitted) {
    log.warn("API Rate limit has exceeded when searching by category. Switching to fallback.",
        notPermitted);
    return jokeFallback.findRandomJokeByCategory(category);
  }

  @Override
  @RateLimiter(name = "norris", fallbackMethod = "findFallbackManyJokes")
  public List<Joke> findManyJokes(String query) {
    List<String> jokes = Optional.ofNullable(chuckNorrisClient.findManyJokes(query))
        .orElseThrow(() -> new JokeNotFoundException(String.format(NOT_FOUND_BY_QUERY_S, query)))
        .getJokeContent();
    List<String> filteredOutJokes = jokes.stream().filter(joke -> satisfiesQuery(joke, query))
        .toList();
    if (filteredOutJokes.isEmpty()) {
      throw new JokeNotFoundException(String.format(NOT_FOUND_BY_QUERY_S, query));
    }
    return filteredOutJokes.stream().map(jokeText -> Joke.builder().joke(jokeText).build())
        .toList();
  }

  @SuppressWarnings("unused")
  private List<Joke> findFallbackManyJokes(String query, RequestNotPermitted notPermitted) {
    log.warn("API Rate limit has exceeded when searching for many jokes. Switching to fallback.",
        notPermitted);
    return jokeFallback.findManyJokes(query);
  }

  private boolean satisfiesQuery(String joke, String query) {
    Objects.requireNonNull(query, "Filter query must be instantiated before filtering applied.");
    Objects.requireNonNull(joke, "Joke text must be instantiated to apply search by query.");
    var lowerCaseQuery = query.toLowerCase().trim();
    return Optional.of(joke)
        .map(String::toLowerCase)
        .map(String::trim)
        .map(jokeText -> jokeText.split("[, ?.!'\"\n\r]+"))
        .map(jokesArray -> new HashSet<>(Arrays.asList(jokesArray)))
        .map(jokesSet -> jokesSet.contains(lowerCaseQuery))
        .orElse(false);
  }
}
