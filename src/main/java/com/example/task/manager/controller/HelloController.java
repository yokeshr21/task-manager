package com.example.task.manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot is running";
    }

    @GetMapping("/hello")
    public String helloworld(){
        return "Hello, World";
    }
}
