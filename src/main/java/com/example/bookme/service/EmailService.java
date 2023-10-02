package com.example.bookme.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String from, String to, String subject, String body);

    void sendResetPasswordEmail(String email, String token) throws MessagingException;
}
