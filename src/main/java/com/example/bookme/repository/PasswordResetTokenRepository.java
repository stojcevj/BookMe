package com.example.bookme.repository;

import com.example.bookme.model.PasswordResetToken;
import com.example.bookme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
    List<PasswordResetToken> findByExpiryDateBefore(Date date);
}
