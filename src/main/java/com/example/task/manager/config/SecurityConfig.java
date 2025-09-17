package com.example.task.manager.config;

import com.example.task.manager.util.JwtAuthFilter;
import jakarta.annotation.ManagedBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {} )
                .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/auth/login").permitAll()
                         .requestMatchers("/Taskmanager/tasks/**").authenticated()
                         .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);

    return http.build();
    }
}
