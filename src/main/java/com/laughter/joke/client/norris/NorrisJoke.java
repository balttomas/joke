package com.laughter.joke.client.norris;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laughter.joke.base.Joke;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NorrisJoke implements Joke {

  private List<String> categories;
  @JsonAlias("icon_url")
  private String iconUrl;
  private String id;
  private String url;
  private String value;

  @Override
  @JsonIgnore
  public List<String> getJokeContent() {
    return Collections.singletonList(value);
  }
}
