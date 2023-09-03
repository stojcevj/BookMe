package com.example.bookme.web;

import com.example.bookme.config.filters.JWTAuthenticationFilter;
import com.example.bookme.model.User;
import com.example.bookme.model.dto.ChangePasswordDto;
import com.example.bookme.model.dto.SignUpDto;
import com.example.bookme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {
    private final JWTAuthenticationFilter authenticationFilter;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(HttpServletRequest request,
                          HttpServletResponse response) throws JsonProcessingException {
        Authentication auth = this.authenticationFilter.attemptAuthentication(request, response);
        try{
            return ResponseEntity.ok().body(this.authenticationFilter.generateJwt(response, auth));
        }catch(Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<User> doRegister(@RequestBody SignUpDto sign){
        return userService.save(sign)
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/edit")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @RequestBody ChangePasswordDto changePasswordDto){
        try{
            return userService.changePassword(authentication, changePasswordDto)
                    .map(user -> ResponseEntity.ok().body(user))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

}
