package com.laughter.joke.api;

import com.laughter.joke.base.Joke;
import com.laughter.joke.mediator.JokeMediatorApi;
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
  private final JokeMediatorApi jokeMediator;

  @GetMapping(value = "/random")
  public Joke findJoke() {
    return jokeMediator.findRandomJoke();
  }

  @GetMapping(value = "/random/{category}")
  public Joke findJokeByCategory(@PathVariable("category") String category) {
    return jokeMediator.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/search")
  public List<Joke> findJokesByQuery(@RequestParam("query") String query) {
    return jokeMediator.findManyJokes(query);
  }

}
