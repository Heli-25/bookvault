package com.bookvault.bookvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookvaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookvaultApplication.class, args);
	}

}
