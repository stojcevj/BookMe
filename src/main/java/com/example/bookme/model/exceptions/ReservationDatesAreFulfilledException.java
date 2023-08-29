package com.example.bookme.model.exceptions;

public class ReservationDatesAreFulfilledException extends RuntimeException{
    public ReservationDatesAreFulfilledException() {
        super("Reservation dates are already reserved.");
    }
}
