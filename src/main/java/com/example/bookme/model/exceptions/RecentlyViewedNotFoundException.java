package com.example.bookme.model.exceptions;

public class RecentlyViewedNotFoundException extends RuntimeException{
    public RecentlyViewedNotFoundException() {
        super("Recently viewed not found.");
    }
}
