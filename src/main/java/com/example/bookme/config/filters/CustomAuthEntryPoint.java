package com.example.bookme.config.filters;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(403);
        response.getWriter()
                .println(LocalDateTime.now() + "\n" + "Access Denied");
    }
}
