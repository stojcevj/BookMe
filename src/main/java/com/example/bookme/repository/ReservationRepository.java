package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByReservationUserOrderByReservationEndDateDesc(User user, Pageable pageable);
    List<Reservation> findAllByReservationProperty(Property property);
    Optional<Reservation> findAllByReservationUserAndReservationPropertyAndReservationStartDateAndReservationEndDateIsBefore(User reservationUser, Property reservationProperty, LocalDateTime reservationStartDate, LocalDateTime afterDate);
}
