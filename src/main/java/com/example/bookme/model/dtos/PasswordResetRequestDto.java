package com.example.bookme.model.dtos;

import lombok.Data;

@Data
public class PasswordResetRequestDto {
    private String token;
    private String password;
}
