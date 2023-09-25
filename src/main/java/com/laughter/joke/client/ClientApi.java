package com.laughter.joke.client;

public interface ClientApi {

  Joke findRandomJoke();

  Joke findRandomJokeByCategory(String category);

  Joke findManyJokes(String query);

}
