package com.laughter.joke.api.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laughter.joke.exception.JokeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class JokesRetrieverExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintValidation(
      ConstraintViolationException ex, WebRequest request) {
    ProblemDetail p = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    p.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
    return ResponseEntity.of(p).build();
  }

  @ExceptionHandler(value = feign.FeignException.class )
  protected ResponseEntity<Object> handleConflict(
      feign.FeignException ex, WebRequest request) throws JsonProcessingException {
    Map<String, String> errorDetails = new ObjectMapper().readValue(ex.contentUTF8(), HashMap.class);
    String errorMessage = Optional.ofNullable(errorDetails.get("message")).orElse(ex.getMessage());
    ProblemDetail p = ProblemDetail
        .forStatusAndDetail(HttpStatus.valueOf(ex.status()), errorMessage);
    p.setTitle(Optional.ofNullable(errorDetails.get("error"))
        .orElse(HttpStatus.valueOf(ex.status()).getReasonPhrase()));
    return ResponseEntity.of(p).build();
  }

  @ExceptionHandler(value = JokeNotFoundException.class)
  protected ResponseEntity<Object> handleJokeNotFoundException(
      JokeNotFoundException ex, WebRequest request) {
    ProblemDetail p = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    p.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
    return ResponseEntity.of(p).build();
  }

  @ExceptionHandler(value = Exception.class)
  protected ResponseEntity<Object> handleAnyException(
      Exception ex, WebRequest request) {
    ProblemDetail p = ProblemDetail
        .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    p.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return ResponseEntity.of(p).build();
  }

}
