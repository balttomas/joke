package com.laughter.joke.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * TODO create exception classes per specifix exception subsets 4XX, 5XX etc. not exposing
 * tech.details but be user friendly
 */
@ControllerAdvice
public class JokesRetrieverExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = feign.FeignException.class )
  protected ResponseEntity<Object> handleConflict(
      feign.FeignException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(),
        new HttpHeaders(), HttpStatus.valueOf(ex.status()), request);
  }

}
