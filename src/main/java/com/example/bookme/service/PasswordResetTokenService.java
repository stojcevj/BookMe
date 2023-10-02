package com.example.bookme.service;

import com.example.bookme.model.PasswordResetToken;
import com.example.bookme.model.User;

import java.util.Date;
import java.util.Optional;

public interface PasswordResetTokenService {
    PasswordResetToken createToken(User user);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteToken(PasswordResetToken token);
    void deleteExpiredTokens(Date now);
}
