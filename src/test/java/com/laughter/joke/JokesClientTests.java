package com.laughter.joke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laughter.joke.api.JokesClient;
import com.laughter.joke.domain.Joke;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JokesClientTests {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private JokesClient jokesClient;

  private MockRestServiceServer mockServer;
  private ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void init() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void contextLoads() throws URISyntaxException, JsonProcessingException {
    mockServer.expect(ExpectedCount.once(),
            requestTo(new URI("https://api.chucknorris.io/jokes/random")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(Joke.builder().value("Funny joke.").build())));

    Joke result = jokesClient.findRandomJoke();

    assertNotNull(result);
    assertEquals("Funny joke.", result.getValue());
  }

}