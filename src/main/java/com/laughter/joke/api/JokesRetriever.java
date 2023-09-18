package com.laughter.joke.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/jokes", produces = MediaType.APPLICATION_JSON_VALUE)
public class JokesRetriever {

  @Autowired
  private JokesClient jokesApi;

  private ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping(value = "/random")
  public String findRandomJoke() throws JsonProcessingException {
    return objectMapper.writeValueAsString(jokesApi.findRandomJoke());
  }

}
