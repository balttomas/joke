package com.laughter.joke.api;

import com.laughter.joke.domain.ChuckNorrisJoke;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jokesproxy", url = "${api.get.base}")
public interface ChuckNorrisProxyClient extends BaseApi<ChuckNorrisJoke> {

  @Override
  @GetMapping(value = "/random", produces = "application/json")
  ChuckNorrisJoke findRandomJoke();

  @Override
  @GetMapping(value = "/random?category={category}", produces = "application/json")
  ChuckNorrisJoke findRandomJokeByCategory(@RequestParam("category") String category);

  @Override
  @GetMapping(value = "/search?query={query}", produces = "application/json")
  List<ChuckNorrisJoke> findJokesByQuery(@RequestParam("query") String query);

}
