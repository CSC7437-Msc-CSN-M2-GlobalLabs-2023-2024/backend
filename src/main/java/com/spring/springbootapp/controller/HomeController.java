package com.spring.springbootapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/")
public class HomeController {

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public String welcomeMessage() {
        return "Hello welcome to Spring Boot Application.";
    }
}