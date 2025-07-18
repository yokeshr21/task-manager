package com.example.task.manager.controller;

import com.example.task.manager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

      String result = authService.validateUser(username, password);

      switch(result){
          case "Username not found":
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

          case "Invalid password":
              return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);

          default:
              return ResponseEntity.ok(result);
      }

    }
}
