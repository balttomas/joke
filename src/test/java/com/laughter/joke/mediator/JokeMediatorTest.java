package com.laughter.joke.mediator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.laughter.joke.base.Joke;
import com.laughter.joke.client.ClientApi;
import com.laughter.joke.client.ClientJoke;
import com.laughter.joke.client.norris.MultipleNorrisJokes;
import com.laughter.joke.client.norris.NorrisJoke;
import com.laughter.joke.exception.JokeNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JokeMediatorTest {

  @InjectMocks
  private JokeMediator service;

  @Mock
  private ClientApi chuckNorrisClient;

  @Test
  void shouldFailWithExpectedExceptionWhenMediatorApiRetrievesNothing() {
    Assertions.assertThrows(JokeNotFoundException.class, () -> service.findRandomJokeByCategory("cat"));
  }

  @Test
  void shouldFindRandomSingleJokeByCategory() {
    String jokeText = "Funny joke by category.";
    String jokeCategory = "category";
    given(chuckNorrisClient.findRandomJokeByCategory(jokeCategory)).willReturn(
        NorrisJoke.builder().value(jokeText).build()
    );

    Joke result = service.findRandomJokeByCategory(jokeCategory);

    verify(chuckNorrisClient, times(1)).findRandomJokeByCategory(jokeCategory);
    assertThat(result).isNotNull();
    assertThat(result.getJoke())
        .isNotBlank()
        .isEqualTo(jokeText);
  }

  @Test
  void shouldFindRandomSingleJoke() {
    String jokeText = "Funny man. Joke.";
    given(chuckNorrisClient.findRandomJoke()).willReturn(
        NorrisJoke.builder().value(jokeText).build()
    );

    Joke result = service.findRandomJoke();

    verify(chuckNorrisClient, times(1)).findRandomJoke();
    assertThat(result).isNotNull();
    assertThat(result.getJoke())
        .isNotBlank()
        .isEqualTo(jokeText);
  }

  @Test
  void shouldRetrieveMultipleJokesFilteredOutByQuery() {
    String filterCriteria = "mAn ";
    given(chuckNorrisClient.findManyJokes(filterCriteria)).willReturn(createJokes());

    List<Joke> result = service.findManyJokes(filterCriteria);

    verify(chuckNorrisClient, times(1)).findManyJokes(filterCriteria);
    assertThat(result).isNotNull().hasSize(6);
    assertThat(result.stream().map(Joke::getJoke))
        .containsExactlyInAnyOrder(
            "Funny man. Joke.",
            "Funny.maN. Joke.",
            "Funny.man? Joke.",
            "Funny.Man! Joke.",
            """
                Funny.man
                fail Joke.""",
            "Super man Joke."
        );

  }

  @Test
  void shouldNotRetrieveAnyJokesFilteredOutByQuery() {
    String filterCriteria = "man";
    given(chuckNorrisClient.findManyJokes(filterCriteria)).willReturn(
        findJokesNotSatisfyingCriteria());

    List<Joke> result = service.findManyJokes(filterCriteria);

    assertThat(result)
        .isNotNull()
        .isEmpty();
  }

  private ClientJoke findJokesNotSatisfyingCriteria() {
    return MultipleNorrisJokes.builder()
        .result(List.of(NorrisJoke.builder().value("Funnyman. Joke.").build(),
            NorrisJoke.builder().value("manmanman Joke.").build(),
            NorrisJoke.builder().value("Superman Joke.").build()))
        .total(3).build();
  }

  private ClientJoke createJokes() {
    var thirdPartyJokes = List.of(
        NorrisJoke.builder().value("Funny man. Joke.").build(),
        NorrisJoke.builder().value("Funnyman. Joke.").build(),
        NorrisJoke.builder().value("Funny.maN. Joke.").build(),
        NorrisJoke.builder().value("Funny.man? Joke.").build(),
        NorrisJoke.builder().value("Funny.Man! Joke.").build(),
        NorrisJoke.builder().value("""
            Funny.man
            fail Joke.""").build(),
        NorrisJoke.builder().value("manmanman Joke.").build(),
        NorrisJoke.builder().value("Superman Joke.").build(),
        NorrisJoke.builder().value("Super man Joke.").build()
    );

    return MultipleNorrisJokes.builder()
        .result(thirdPartyJokes)
        .total(thirdPartyJokes.size())
        .build();
  }

}
