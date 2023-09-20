package com.laughter.joke.api.norris;

import com.laughter.joke.api.BaseApi;
import com.laughter.joke.domain.ChuckNorrisJoke;
import com.laughter.joke.domain.ManyChuckNorrisJokes;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "chuckNorrisClient", url = "${chucknorris.jokes.url}")
public interface ChuckNorrisClient extends BaseApi<ChuckNorrisJoke> {

  @Override
  @GetMapping(value = "/random", consumes = "application/json", produces = "application/json")
  ChuckNorrisJoke findRandomJoke();

  @Override
  @GetMapping(value = "/random", consumes = "application/json", produces = "application/json")
  ChuckNorrisJoke findRandomJokeByCategory(@RequestParam("category") String category);

  @Override
  @GetMapping(value = "/search", consumes = "application/json", produces = "application/json")
  ManyChuckNorrisJokes findJokesByQuery(@RequestParam("query") String query);

}
