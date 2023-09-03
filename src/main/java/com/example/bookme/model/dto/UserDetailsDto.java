package com.example.bookme.model.dto;

import com.example.bookme.model.User;
import com.example.bookme.model.enumertaion.Role;
import lombok.Data;

@Data
public class UserDetailsDto {
    private Long id;
    private String username;
    private String firstName;
    private Role role;

    public static UserDetailsDto of(User user) {
        UserDetailsDto details = new UserDetailsDto();
        details.id = user.getId();
        details.username = user.getUsername();
        details.firstName = user.getFirstName();
        details.role = user.getRole();
        return details;
    }
}


