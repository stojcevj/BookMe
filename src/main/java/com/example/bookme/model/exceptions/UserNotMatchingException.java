package com.example.bookme.model.exceptions;

public class UserNotMatchingException extends RuntimeException{
    public UserNotMatchingException() {
        super("Logged in user and property user do not match.");
    }
}
