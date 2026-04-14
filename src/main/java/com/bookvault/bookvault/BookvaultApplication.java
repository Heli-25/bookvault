package com.bookvault.bookvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class BookvaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookvaultApplication.class, args);
	}

}
