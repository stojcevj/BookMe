package com.example.bookme.tasks;

import com.example.bookme.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class TokenCleanupTask {

    private final PasswordResetTokenService tokenService;

    @Scheduled(cron = "0 0 1 * * ?") // Run at 1:00 AM daily
    public void cleanupExpiredTokens(){
        Date now = new Date();
        tokenService.deleteExpiredTokens(now);
    }
}
