package com.wiremockexample;

import com.wiremockexample.config.GithubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = GithubProperties.class)
public class SpringBootWiremockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWiremockApplication.class, args);
	}

}
