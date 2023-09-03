package com.example.bookme.model.exceptions;

public class ReservationExceedsPropertyCapacityException extends RuntimeException{
    public ReservationExceedsPropertyCapacityException() {
        super("Reservation exceeds property capacity");
    }
}
