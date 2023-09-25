package com.laughter.joke.mediator;

import com.laughter.joke.base.Joke;
import java.util.List;

public interface JokeMediatorApi {

  Joke findRandomJoke();

  Joke findRandomJokeByCategory(String category);

  List<Joke> findManyJokes(String query);

}
