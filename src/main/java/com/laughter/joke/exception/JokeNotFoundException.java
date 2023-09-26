package com.laughter.joke.exception;

public class JokeNotFoundException extends RuntimeException {

  public JokeNotFoundException() {
  }

  public JokeNotFoundException(String message) {
    super(message);
  }

  public JokeNotFoundException(String message, Throwable e) {
    super(message, e);
  }
}
