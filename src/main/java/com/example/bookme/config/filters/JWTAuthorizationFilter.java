package com.example.bookme.config.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bookme.config.JWTAuthConstants;
import com.example.bookme.model.dto.UserDetailsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTAuthConstants.HEADER_STRING);
        if (header==null || !header.startsWith(JWTAuthConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken token = getToken(header);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    public UsernamePasswordAuthenticationToken getToken(String header) throws JsonProcessingException {
        // parse the token.
        try {
            String user = JWT.require(Algorithm.HMAC256(JWTAuthConstants.SECRET.getBytes()))
                    .build()
                    .verify(header.replace(JWTAuthConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user == null) {
                return null;
            }
            UserDetailsDto userDetails = new ObjectMapper().readValue(user, UserDetailsDto.class);
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "", Collections.singleton(userDetails.getRole()));

        }catch(Exception e) {
            return null;
        }
    }
}

