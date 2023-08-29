package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationProperty(Property property);
}
