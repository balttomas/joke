package com.laughter.joke.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private JokesClient jokesApi;

  @Autowired
  private BaseApi<ChuckNorrisJoke> chuckNorrisProxyClient;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping(value = "/random")
  public String findRandomJoke() throws JsonProcessingException {
    return objectMapper.writeValueAsString(jokesApi.findRandomJoke());
  }

  @GetMapping(value = "/proxy/random")
  //@CircuitBreaker with fallback
  public Joke findJoke() {
    return chuckNorrisProxyClient.findRandomJoke();
  }

  @GetMapping(value = "/proxy/random/{category}")
  public Joke findJokeByCategory(@PathVariable("category") String category) {
    return chuckNorrisProxyClient.findRandomJokeByCategory(category);
  }

  @GetMapping(value = "/proxy/search?query={query}")
  public List<? extends Joke> findJokesByQuery(@RequestParam("query") String query) {
    return chuckNorrisProxyClient.findJokesByQuery(query);
  }

}
