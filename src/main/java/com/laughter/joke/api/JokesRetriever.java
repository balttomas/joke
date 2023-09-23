package com.laughter.joke.api;

import com.laughter.joke.base.Joke;
import com.laughter.joke.client.norris.ChuckNorrisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/jokes", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokesRetriever {

  @Autowired
  private ChuckNorrisClient chuckNorrisClient;

  @GetMapping(value = "/random")
  public Joke findJoke() {
    return chuckNorrisClient.findRandomJoke();
  }

  @GetMapping(value = "/random/{category}")
  public Joke findJokeByCategory(@PathVariable("category") String category) {
    return chuckNorrisClient.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/search")
  public Joke findJokesByQuery(@RequestParam("query") String query) {
    return chuckNorrisClient.findManyJokes(query);
  }

}
