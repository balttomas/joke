package com.laughter.joke.api;

import com.laughter.joke.domain.ChuckNorrisJoke;
import com.laughter.joke.domain.Joke;
import java.util.List;
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
  private BaseApi<ChuckNorrisJoke> chuckNorrisProxyClient;

  @GetMapping(value = "/random")
  public Joke findJoke() {
    return chuckNorrisProxyClient.findRandomJoke();
  }

  @GetMapping(value = "/random/{category}")
  public Joke findJokeByCategory(@PathVariable("category") String category) {
    return chuckNorrisProxyClient.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/search?query={query}")
  public List<? extends Joke> findJokesByQuery(@RequestParam("query") String query) {
    return chuckNorrisProxyClient.findJokesByQuery(query);
  }

}
