package com.example.bookme.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    private Long userId;
    private String userToken;
    private String userNewPassword;
    private String userNewPasswordConfirm;

}
