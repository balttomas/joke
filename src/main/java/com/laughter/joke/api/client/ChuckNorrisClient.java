package com.laughter.joke.api.client;

import com.laughter.joke.domain.ChuckNorrisJoke;
import com.laughter.joke.domain.ManyChuckNorrisJokes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "chuckNorrisClient", url = "${chucknorris.jokes.url}")
public interface ChuckNorrisClient {

  @GetMapping(value = "/random", consumes = "application/json", produces = "application/json")
  ChuckNorrisJoke findRandomJoke();

  @GetMapping(value = "/random", consumes = "application/json", produces = "application/json")
  ChuckNorrisJoke findRandomJokeByCategory(@RequestParam("category") String category);

  @GetMapping(value = "/search", consumes = "application/json", produces = "application/json")
  ManyChuckNorrisJokes findManyJokes(@RequestParam("query") String query);

}
