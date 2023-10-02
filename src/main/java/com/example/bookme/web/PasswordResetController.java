package com.example.bookme.web;

import com.example.bookme.model.PasswordResetToken;
import com.example.bookme.model.User;
import com.example.bookme.service.EmailService;
import com.example.bookme.service.PasswordResetTokenService;
import com.example.bookme.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reset-password")
@AllArgsConstructor
@CrossOrigin("*")
public class PasswordResetController {
    private PasswordResetTokenService tokenService;
    private UserService userService;
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        PasswordResetToken resetToken = tokenService.createToken(user);

        try {
            emailService.sendResetPasswordEmail(user.getEmail(), resetToken.getToken());
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset email. Please try again.");
        }
    }




}
