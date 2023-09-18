package com.laughter.joke.api;

import com.laughter.joke.domain.Joke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/jokes", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokesRetriever {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.get.random}")
  private String jokeUrl;

  @GetMapping(value = "/random")
  public Joke findRandomJoke() {
    try {
      ResponseEntity<Joke> response = restTemplate.getForEntity(jokeUrl, Joke.class);
      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
