package com.laughter.joke.mediator;

import java.util.List;

public interface JokesMediatorApi {

  String findRandomJoke();

  String findRandomJokeByCategory(String category);

  List<String> findManyJokes(String query);

}
