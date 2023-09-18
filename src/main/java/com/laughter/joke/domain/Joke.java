package com.laughter.joke.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Joke {
  private String icon_url;
  private String id;
  private String url;
  private String value;
}
