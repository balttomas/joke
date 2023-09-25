package com.laughter.joke.client.norris;

import com.laughter.joke.client.ClientApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "chuckNorrisClient", url = "${chucknorris.jokes.url}")
public interface ChuckNorrisClient extends ClientApi {

  @GetMapping(value = "/random", produces = "application/json")
  NorrisJoke findRandomJoke();

  @GetMapping(value = "/random", produces = "application/json")
  NorrisJoke findRandomJokeByCategory(@RequestParam("category") String category);

  @GetMapping(value = "/search", produces = "application/json")
  MultipleNorrisJokes findManyJokes(@RequestParam("query") String query);

}
