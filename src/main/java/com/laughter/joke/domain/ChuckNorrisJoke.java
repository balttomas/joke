package com.laughter.joke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChuckNorrisJoke implements Joke {

  private List<String> categories;
  private String icon_url;
  private String id;
  private String url;
  private String value;

  @Override
  @JsonIgnore
  public String getJokeValue() {
    return value;
  }
}
