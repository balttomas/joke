package com.laughter.joke;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClient {

  @Bean
  public RestTemplate restTemplate(@Value("${connection.timeout.ms:3000}") long connectionTimeout,
      @Value("${read.timeout.ms:3000}") long readTimeout) {
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(connectionTimeout))
        .setReadTimeout(Duration.ofMillis(readTimeout))
        .build();
  }

}
