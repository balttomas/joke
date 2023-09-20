package com.laughter.joke.api;

import org.springframework.web.bind.annotation.RequestParam;

public interface BaseApi<S, M> {

  S findRandomJoke();

  S findRandomJokeByCategory(@RequestParam("category") String category);

  M findManyJokes(@RequestParam("query") String query);

}
