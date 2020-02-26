package com.bdi.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ConfigurationProperties("com.bdi.sb.config")
@Configuration
public class ChattingSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattingSpringBootApplication.class, args);
	}

}
