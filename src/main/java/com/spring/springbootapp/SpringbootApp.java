package com.spring.springbootapp;

import com.spring.springbootapp.Service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringbootApp {
	@Autowired
	private InitService initService;
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApp.class, args);
	}

	@PostConstruct
	public void init() {
		initService.init();
	}
}
