package com.example.task.manager.controller;

import com.example.task.manager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials){

        String username = credentials.get("username");
        String password = credentials.get("password");

        if(authService.validateUser(username,password)){
            return ResponseEntity.ok("Login successful");
        }
        else{
            return ResponseEntity.status(401).body("Invalid Username and Password");
        }

    }
}
