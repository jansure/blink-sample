package com.glory.blink.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class BlinkServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BlinkServerApplication.class, args);
	}
}
