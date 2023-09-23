package com.laughter.joke.api;

import com.laughter.joke.mediator.JokesMediatorApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/jokes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class JokesRetriever {

  @Autowired
  private final JokesMediatorApi jokeService;

  @GetMapping(value = "/random")
  public String findJoke() {
    return jokeService.findRandomJoke();
  }

  @GetMapping(value = "/random/{category}")
  public String findJokeByCategory(@PathVariable("category") String category) {
    return jokeService.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/search")
  public List<String> findJokesByQuery(@RequestParam("query") String query) {
    return jokeService.findManyJokes(query);
  }

}
