package com.edigest.authservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	@Value("${spring.datasource.url}")
	private static String db_url;
	public static void main(String[] args) {
		System.out.println(db_url);
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
