package com.laughter.joke.base;

public interface ClientApi {

  Joke findRandomJoke();

  Joke findRandomJokeByCategory(String category);

  Joke findManyJokes(String query);

}
