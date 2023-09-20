package com.laughter.joke.api.config;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class BaseClientApiConfiguration {

  private final ErrorDecoder defaultErrorDecoder = new Default();

  @Bean
  public ErrorDecoder errorDecoder() {
    return (methodKey, response) -> {
      Exception exception = defaultErrorDecoder.decode(methodKey, response);
      if(exception instanceof RetryableException){
        return exception;
      }
      if (HttpStatus.valueOf(response.status()).is5xxServerError()) {
        return new RetryableException(response.status(), "Server 5XX error.",
            response.request().httpMethod(), exception, null, response.request());
      }
      return exception;
    };
  }

  @Bean
  public Retryer retryer() {
    return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
  }

}
