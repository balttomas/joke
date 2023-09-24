package com.laughter.joke.mediator;

import com.laughter.joke.base.ClientApi;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JokeService implements JokesMediatorApi {

  @Autowired
  private final ClientApi chuckNorrisClient;

  @Override
  @RateLimiter(name = "jokes")
  public String findRandomJoke() {
    return chuckNorrisClient.findRandomJoke().getJokeContent().get(0);
  }

  @Override
  @RateLimiter(name = "jokes")
  public String findRandomJokeByCategory(String category) {
    return chuckNorrisClient.findRandomJokeByCategory(category).getJokeContent().get(0);
  }

  @Override
  @RateLimiter(name = "jokes")
  public List<String> findManyJokes(String query) {
    List<String> jokes = chuckNorrisClient.findManyJokes(query).getJokeContent();

/*    String lowerCaseQuery = query.toLowerCase();
    String[] criteria = {" " + lowerCaseQuery, "," + lowerCaseQuery,
        "?" + lowerCaseQuery, "." + lowerCaseQuery, "!" + lowerCaseQuery};
    return jokes.stream()
        .filter(joke -> StringUtils.indexOfAny(joke.toLowerCase(), criteria) > -1)
        .toList();*/

    return jokes.stream().filter(joke -> satisfiesQuery(joke, query)).toList();

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
