package com.example.task.manager.controller;

import com.example.task.manager.service.AuthService;
import com.example.task.manager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (authService.validateUser(username, password)) {
             String token = jwtUtil.generateToken(username);
             Map<String, String> map = new HashMap<>();
             map.put("token", token);
             return ResponseEntity.ok(map);
        }
        else{
            return ResponseEntity.status(401).body("Invalid Username and Password");
        }
    }
}
