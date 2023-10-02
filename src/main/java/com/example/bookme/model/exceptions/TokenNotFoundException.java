package com.example.bookme.model.exceptions;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(){
        super("Token was not found.");
    }
}
