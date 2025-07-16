package com.example.task.manager.service;

import com.example.task.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }

}
