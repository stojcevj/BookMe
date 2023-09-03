package com.example.bookme.service;

import com.example.bookme.model.Reservation;
import com.example.bookme.model.dto.ReservationAddDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Page<Reservation> getAllReservationsForUser(Authentication authentication,
                                                Pageable pageable);
    Optional<Reservation> addReservation(Authentication authentication,
                                         ReservationAddDto reservationAddDto);
    Optional<Reservation> deleteReservation(Long id,
                                            Authentication authentication);
}
