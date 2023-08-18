package com.prgrms.moviechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MovieCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCheckerApplication.class, args);
	}

}
