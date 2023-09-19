package com.laughter.joke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokeApplication.class, args);
	}

}
