package com.laughter.joke.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManyChuckNorrisJokes {

  private int total;
  private List<ChuckNorrisJoke> result;

}
