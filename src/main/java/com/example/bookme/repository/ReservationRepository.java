package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationProperty(Property property);
    List<Reservation> findAllByReservationStartDateGreaterThanAndReservationStartDateLessThanOrReservationEndDateGreaterThanAndReservationEndDateLessThan(LocalDateTime startDate,
                                                                                                                                                          LocalDateTime endDate,
                                                                                                                                                          LocalDateTime startDate1,
                                                                                                                                                          LocalDateTime endDate1);
    List<Reservation> findAllByReservationProperty_PropertyCityLikeIgnoreCaseAndReservationStartDateGreaterThanAndReservationStartDateLessThanOrReservationEndDateGreaterThanAndReservationEndDateLessThan(String city,
                                                                                                                                                                                                           LocalDateTime startDate,
                                                                                                                                                                                                           LocalDateTime endDate,
                                                                                                                                                                                                           LocalDateTime startDate1,
                                                                                                                                                                                                           LocalDateTime endDate1);
}
