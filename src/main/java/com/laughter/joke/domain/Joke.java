package com.laughter.joke.domain;

import lombok.Data;

@Data
public class Joke {
  private String icon_url;
  private String id;
  private String url;
  private String value;
}
