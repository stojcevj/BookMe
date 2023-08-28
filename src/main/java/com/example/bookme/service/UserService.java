package com.example.bookme.service;

import com.example.bookme.model.User;
import com.example.bookme.model.dto.ChangePasswordDto;
import com.example.bookme.model.dto.SignUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> save(SignUpDto sign);
    Optional<User> changePassword(Authentication authentication,
                                  ChangePasswordDto changePasswordDto) throws JsonProcessingException;
}
