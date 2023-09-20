package com.laughter.joke.api.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
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

/*  @Bean
  public Decoder customDecoder(ObjectFactory<HttpMessageConverters> objectFactory,
      ObjectProvider<HttpMessageConverterCustomizer> customizers) {
    Decoder decoder = (response, type) -> {
      if (type.getTypeName().equals(org.springframework.core.io.Resource.class.getName())) {
        return new SpringDecoder(objectFactory, customizers).decode(response, type);
      }
      return new JacksonDecoder(List.of(new JavaTimeModule())).decode(response, type);
    };

    return new ResponseEntityDecoder(decoder);
  }*/

}
