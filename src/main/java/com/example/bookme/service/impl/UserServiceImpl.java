package com.example.bookme.service.impl;

import com.example.bookme.model.User;
import com.example.bookme.model.dto.SignUpDto;
import com.example.bookme.model.enumertaion.Role;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Optional<User> save(SignUpDto sign) {
        User user = User.builder().firstName(sign.getFirstName()).lastName(sign.getLastName())
                .mobilePhone(sign.getMobilePhone()).email(sign.getEmail())
                .password(passwordEncoder.encode(sign.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return Optional.of(user);
    }
}
