package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByReservationUser(User user, Pageable pageable);
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
