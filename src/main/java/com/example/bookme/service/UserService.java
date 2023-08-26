package com.example.bookme.service;

import com.example.bookme.model.User;
import com.example.bookme.model.dto.SignUpDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> save(SignUpDto sign);
}
