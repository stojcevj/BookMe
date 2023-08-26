package com.example.bookme.model.exceptions;

public class AnonymousUserException extends RuntimeException{
    public AnonymousUserException(){
        super("User not logged in.");
    }
}
