package com.laughter.joke.mediator;

import com.laughter.joke.base.Joke;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JokeFallback implements JokeMediatorApi {

  @Override
  public Joke findRandomJoke() {
    return new Joke(
        "You're so eager for huge amount of jokes. Be patient. Wait a second and try receiving some again.");
  }

  @Override
  public Joke findRandomJokeByCategory(String category) {
    return new Joke(String.format(
        "Jokes of type %s are so awesome. Be patient. Wait a second and try receiving some again.",
        category));
  }

  @Override
  public List<Joke> findManyJokes(String query) {
    return Collections.singletonList(new Joke(
        String.format(
            "Your search for jokes by criteria %s is busy a bit. Be patient. Wait a second and try receiving some again.",
            query))
    );
  }
}
