package com.laughter.joke.client.norris;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laughter.joke.base.Joke;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipleNorrisJokes implements Joke {

  private int total;
  private List<NorrisJoke> result = new ArrayList<>();

  @Override
  @JsonIgnore
  public List<String> getJokeContent() {
    return result.stream().map(NorrisJoke::getValue).toList();
  }
}
