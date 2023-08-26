package com.example.bookme.web;

import com.example.bookme.config.filters.JWTAuthenticationFilter;
import com.example.bookme.model.User;
import com.example.bookme.model.dto.SignUpDto;
import com.example.bookme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class UserController {
    private final JWTAuthenticationFilter authenticationFilter;
    private final UserService userService;

    @PostMapping("/signin")
    public String doLogin(HttpServletRequest request,
                          HttpServletResponse response) throws JsonProcessingException {
        Authentication auth = this.authenticationFilter.attemptAuthentication(request, response);
        return this.authenticationFilter.generateJwt(response, auth);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> doRegister(@RequestBody SignUpDto sign){
        return userService.save(sign)
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
