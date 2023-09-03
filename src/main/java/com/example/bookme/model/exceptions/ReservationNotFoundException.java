package com.example.bookme.model.exceptions;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException() {
        super("Reservation not found");
    }
}
