package com.example.bookme.service.impl;

import com.example.bookme.model.User;
import com.example.bookme.model.dto.ChangePasswordDto;
import com.example.bookme.model.dto.SignUpDto;
import com.example.bookme.model.enumertaion.Role;
import com.example.bookme.model.exceptions.PasswordDoNotMatchException;
import com.example.bookme.model.exceptions.UserIdNotMatchingException;
import com.example.bookme.model.exceptions.UserNotFoundException;
import com.example.bookme.model.exceptions.UserNotMatchingException;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.UserService;
import com.example.bookme.utils.TokenParseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    @Override
    public Optional<User> changePassword(Authentication authentication,
                                         ChangePasswordDto changePasswordDto) throws JsonProcessingException {
        User authenticatedUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        String email = TokenParseUtil.parseToken(changePasswordDto.getUserToken());

        if(email == null){
            throw new UsernameNotFoundException("User Token Not Found.");
        }

        User changePasswordUser = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if(changePasswordUser != authenticatedUser){
            throw new UserNotMatchingException();
        }

        if(!Objects.equals(authenticatedUser.getId(), changePasswordDto.getUserId())){
            throw new UserIdNotMatchingException();
        }

        if(!changePasswordDto.getUserNewPassword().equals(changePasswordDto.getUserNewPasswordConfirm())){
            throw new PasswordDoNotMatchException();
        }

        authenticatedUser.setPassword(changePasswordDto.getUserNewPassword());

        return Optional.of(userRepository.save(authenticatedUser));
    }
}
