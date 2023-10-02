package com.example.bookme.service.impl;

import com.example.bookme.model.PasswordResetToken;
import com.example.bookme.model.User;
import com.example.bookme.repository.PasswordResetTokenRepository;
import com.example.bookme.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final int EXPIRATION_HOURS = 24;

    private final PasswordResetTokenRepository tokenRepository;

    @Override
    public PasswordResetToken createToken(User user) {
        PasswordResetToken userToken = tokenRepository.findByUser(user).orElse(null);

        String token = generateToken();
        if(userToken != null){
            userToken.setToken(token);
            userToken.setExpiryDate(calculateExpiryDate());
            return tokenRepository.save(userToken);
        }
        else{
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(calculateExpiryDate());
            return tokenRepository.save(resetToken);
        }
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteExpiredTokens(Date now) {
        List<PasswordResetToken> expiredToken = tokenRepository.findByExpiryDateBefore(now);
        tokenRepository.deleteAll(expiredToken);
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, EXPIRATION_HOURS);
        return calendar.getTime();
    }
}
