package com.example.bookme.service;

import com.example.bookme.model.Reservation;
import com.example.bookme.model.dto.ReservationAddDto;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface ReservationService {
    Optional<Reservation> addReservation(Authentication authentication,
                                         ReservationAddDto reservationAddDto);
}
