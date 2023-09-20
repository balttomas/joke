package com.laughter.joke.api;

import com.laughter.joke.api.norris.ChuckNorrisClient;
import com.laughter.joke.domain.ChuckNorrisJoke;
import com.laughter.joke.domain.ManyChuckNorrisJokes;
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
  public ChuckNorrisJoke findJoke() {
    return chuckNorrisClient.findRandomJoke();
  }

  @GetMapping(value = "/random/{category}")
  public ChuckNorrisJoke findJokeByCategory(@PathVariable("category") String category) {
    return chuckNorrisClient.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/search")
  public ManyChuckNorrisJokes findJokesByQuery(@RequestParam("query") String query) {
    return chuckNorrisClient.findManyJokes(query);
  }

}
