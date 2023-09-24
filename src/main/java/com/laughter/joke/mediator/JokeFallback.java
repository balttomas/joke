package com.laughter.joke.mediator;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JokeFallback implements JokeMediatorApi {

  @Override
  public String findRandomJoke() {
    return "You're so eager for huge amount of jokes. Be patient. Wait a second and try receiving some again.";
  }

  @Override
  public String findRandomJokeByCategory(String category) {
    return String.format(
        "Jokes of type %s are so awesome. Be patient. Wait a second and try receiving some again.",
        category);
  }

  @Override
  public List<String> findManyJokes(String query) {
    return Collections.singletonList(
        String.format(
            "Your search for jokes by criteria %s is busy a bit. Be patient. Wait a second and try receiving some again.",
            query)
    );
  }
}
