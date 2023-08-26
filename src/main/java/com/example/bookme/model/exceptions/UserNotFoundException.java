package com.example.bookme.model.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found exception");
    }
}
