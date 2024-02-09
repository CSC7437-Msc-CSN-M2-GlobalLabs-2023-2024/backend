package com.spring.springbootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost")
public class SpringbootApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApp.class, args);
	}
}
