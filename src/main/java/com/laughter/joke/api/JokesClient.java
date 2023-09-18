package com.laughter.joke.api;

import com.laughter.joke.domain.Joke;
import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokesClient {

  private final RestTemplate restTemplate;

  @Value("${api.get.random}")
  private String jokeUrl;

  public JokesClient(@Value("${connection.timeout.ms:3000}") long connectionTimeout,
      @Value("${read.timeout.ms:3000}") long readTimeout) {
    restTemplate = new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(connectionTimeout))
        .setReadTimeout(Duration.ofMillis(readTimeout))
        .build();
  }

  public String findRandomJoke() {
    ResponseEntity<Joke> response = restTemplate.getForEntity(jokeUrl, Joke.class);
    return Optional.ofNullable(response).map(ResponseEntity::getBody).map(Joke::getValue)
        .orElse("You're provided with some backup joke.");
  }

}
