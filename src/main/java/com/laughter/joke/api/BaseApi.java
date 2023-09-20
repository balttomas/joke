package com.laughter.joke.api;

import com.laughter.joke.domain.Joke;
import com.laughter.joke.domain.ManyChuckNorrisJokes;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

public interface BaseApi<T extends Joke> {

  T findRandomJoke();

  T findRandomJokeByCategory(@RequestParam("category") String category);

  ManyChuckNorrisJokes findJokesByQuery(@RequestParam("query") String query);

}
