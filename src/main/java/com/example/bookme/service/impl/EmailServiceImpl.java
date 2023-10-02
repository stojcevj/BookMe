package com.example.bookme.service.impl;

import com.example.bookme.model.exceptions.UserNotFoundException;
import com.example.bookme.service.EmailService;
import com.example.bookme.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Override
    public void sendEmail(String from, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void sendResetPasswordEmail(String toEmail, String token) throws MessagingException {
        String name = userService.findByEmail(toEmail).orElseThrow(UserNotFoundException::new).getFirstName();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Password Reset");

        String resetUrl = "https://localhost/reset-password?token=" + token;

        String emailBody = "Hi, " + name + ",<br/><br/>"
                + "You requested to reset your password for your BookMe account.<br/><br/>"
                + "Please click this link to reset your password:<br/>"
                + "<a href=\"" + resetUrl + "\">Reset Password</a><br/><br/>"
                + "Thanks,<br/>"
                + "The BookMe Team";

        helper.setText(emailBody, true);

        javaMailSender.send(message);
    }
}
