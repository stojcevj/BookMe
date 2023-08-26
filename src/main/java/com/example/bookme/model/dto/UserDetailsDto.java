package com.example.bookme.model.dto;

import com.example.bookme.model.User;
import com.example.bookme.model.enumertaion.Role;
import lombok.Data;

@Data
public class UserDetailsDto {
    private String username;
    private Role role;

    public static UserDetailsDto of(User user) {
        UserDetailsDto details = new UserDetailsDto();
        details.username = user.getUsername();
        details.role = user.getRole();
        return details;
    }
}


