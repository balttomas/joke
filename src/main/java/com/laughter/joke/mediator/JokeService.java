package com.laughter.joke.mediator;

import com.laughter.joke.base.ClientApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JokeService implements JokesMediatorApi {

  @Autowired
  private final ClientApi chuckNorrisClient;

  @Override
  public String findRandomJoke() {
    return chuckNorrisClient.findRandomJoke().getJokeContent().get(0);
  }

  @Override
  public String findRandomJokeByCategory(String category) {
    return chuckNorrisClient.findRandomJokeByCategory(category).getJokeContent().get(0);
  }

  @Override
  public List<String> findManyJokes(String query) {
    return chuckNorrisClient.findManyJokes(query).getJokeContent();
  }
}
