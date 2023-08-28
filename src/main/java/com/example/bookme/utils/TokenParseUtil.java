package com.example.bookme.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bookme.config.JWTAuthConstants;
import com.example.bookme.model.dto.UserDetailsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenParseUtil {
    public static String parseToken(String token) throws JsonProcessingException {
        try {
            String user = JWT.require(Algorithm.HMAC256(JWTAuthConstants.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JWTAuthConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user == null) {
                return null;
            }
            UserDetailsDto userDetails = new ObjectMapper().readValue(user, UserDetailsDto.class);

            return userDetails.getUsername();
        }catch (Exception e){
            return null;
        }
    }
}
