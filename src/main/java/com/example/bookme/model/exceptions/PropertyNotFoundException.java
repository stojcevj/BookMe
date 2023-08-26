package com.example.bookme.model.exceptions;

public class PropertyNotFoundException extends RuntimeException{
    public PropertyNotFoundException() {
        super("Property not found exception");
    }
}

