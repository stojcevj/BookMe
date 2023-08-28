package com.example.bookme.model.exceptions;

public class PasswordDoNotMatchException extends RuntimeException{
    public PasswordDoNotMatchException() {
        super("Password's do not match");
    }
}
