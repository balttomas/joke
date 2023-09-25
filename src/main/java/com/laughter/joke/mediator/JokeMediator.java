package com.laughter.joke.mediator;

import com.laughter.joke.base.Joke;
import com.laughter.joke.client.ClientApi;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokeMediator implements JokeMediatorApi {

  @Autowired
  private final ClientApi chuckNorrisClient;

  @Autowired
  private final JokeMediatorApi jokeFallback;

  @Override
  @RateLimiter(name = "norris", fallbackMethod = "findFallbackRandomJoke")
  public Joke findRandomJoke() {
    return Joke.builder().joke(
        chuckNorrisClient.findRandomJoke().getJokeContent().get(0)).build();
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
        .joke(chuckNorrisClient.findRandomJokeByCategory(category).getJokeContent().get(0)).build();
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
    List<String> jokes = chuckNorrisClient.findManyJokes(query).getJokeContent();
    List<String> filteredOutJokes = jokes.stream().filter(joke -> satisfiesQuery(joke, query))
        .toList();
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
    var lowerCaseQuery = query.toLowerCase();
    return Optional.of(joke)
        .map(String::toLowerCase)
        .map(jokeText -> jokeText.split("[, ?.!'\"\n\r]+"))
        .map(jokesArray -> new HashSet<>(Arrays.asList(jokesArray)))
        .map(jokesSet -> jokesSet.contains(lowerCaseQuery))
        .orElse(false);
  }
}
