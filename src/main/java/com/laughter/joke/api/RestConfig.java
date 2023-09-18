package com.laughter.joke.api;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

  @Bean
  public RestTemplate restTemplate(@Value("${connection.timeout.ms}") long connTimeout,
      @Value("${read.timeout.ms}") long readTimeout) {
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(connTimeout))
        .setReadTimeout(Duration.ofMillis(readTimeout))
        .build();
  }

}
