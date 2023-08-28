package com.example.bookme.model.exceptions;

public class UserIdNotMatchingException extends RuntimeException{
    public UserIdNotMatchingException() {
        super("User Id`s Not Matching");
    }
}
