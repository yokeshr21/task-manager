package com.example.task.manager.service;

import com.example.task.manager.model.Users;
import com.example.task.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String validateUser(String username, String password){
        Optional<Users> userOpt = userRepository.findByUsername(username);

        if(userOpt.isEmpty()){
            return "Username not found";
        }

        Users users = userOpt.get();

        if(!users.getPassword().equals(password)){
            return "Invalid password";
        }

        return "Login successful";
    }
}
