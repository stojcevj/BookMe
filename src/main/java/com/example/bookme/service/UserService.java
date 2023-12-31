package com.example.bookme.service;

import com.example.bookme.model.User;
import com.example.bookme.model.dtos.ChangePasswordDto;
import com.example.bookme.model.dtos.PasswordResetRequestDto;
import com.example.bookme.model.dtos.SignUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> save(SignUpDto sign);
    Optional<User> changePassword(Authentication authentication,
                                  ChangePasswordDto changePasswordDto) throws JsonProcessingException;
    Optional<User> resetPassword(PasswordResetRequestDto request);
    Optional<User> findByEmail(String email);
}
