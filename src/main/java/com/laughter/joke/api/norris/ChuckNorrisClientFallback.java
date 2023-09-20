package com.laughter.joke.api.norris;

import com.laughter.joke.domain.ChuckNorrisJoke;
import com.laughter.joke.domain.ManyChuckNorrisJokes;
import java.util.List;

/**
 * TODO find-out how to enable fallback (hystrix/circuitbreaker)
 */
//@Component
public class ChuckNorrisClientFallback implements ChuckNorrisClient {

  @Override
  public ChuckNorrisJoke findRandomJoke() {
    return ChuckNorrisJoke.builder()
        .value("We can't retrieve any joke but here it is some local joke for you!")
        .build();
  }

  @Override
  public ChuckNorrisJoke findRandomJokeByCategory(String category) {
    return ChuckNorrisJoke.builder()
        .value(String.format("We can't retrieve any joke but here it is some local joke "
            + "from category %s for you!", category))
        .build();
  }

  @Override
  public ManyChuckNorrisJokes findManyJokes(String query) {
    return ManyChuckNorrisJokes.builder().result(
        List.of(ChuckNorrisJoke.builder()
            .value("Jokes generate vitamin C.")
            .build())
    ).build();

  }
}
