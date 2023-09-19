package com.laughter.joke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
