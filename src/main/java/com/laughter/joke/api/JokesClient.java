package com.laughter.joke.api;

import com.laughter.joke.domain.Joke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokesClient {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.get.random}")
  private String jokeUrl;

  public Joke findRandomJoke() {
    return restTemplate.getForEntity(jokeUrl, Joke.class).getBody();
/*    return Optional.ofNullable(restTemplate.getForEntity(jokeUrl, Joke.class))
        .map(ResponseEntity::getBody)
        .orElse(Joke.builder().value("A very funny joke. Just in case.").build());*/
  }

}
