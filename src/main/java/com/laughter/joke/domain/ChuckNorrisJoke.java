package com.laughter.joke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChuckNorrisJoke implements Joke {

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
