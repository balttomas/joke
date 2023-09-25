package com.laughter.joke.client;

public interface ClientApi {

  ClientJoke findRandomJoke();

  ClientJoke findRandomJokeByCategory(String category);

  ClientJoke findManyJokes(String query);

}
