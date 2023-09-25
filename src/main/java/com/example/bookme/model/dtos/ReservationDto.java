package com.example.bookme.model.dtos;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ReservationDto {
    private Long id;
    private Long reservationPropertyId;
    private String reservationPropertyName;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private Integer reservationNumberOfPeople;
    private Double reservationTotalPrice;

    public static ReservationDto of(Reservation reservation){

        return ReservationDto.builder()
                .id(reservation.getId())
                .reservationPropertyId(reservation.getReservationProperty().getId())
                .reservationPropertyName(reservation.getReservationProperty().getPropertyName())
                .reservationStartDate(reservation.getReservationStartDate())
                .reservationEndDate(reservation.getReservationEndDate())
                .reservationNumberOfPeople(reservation.getReservationNumberOfPeople())
                .reservationTotalPrice(reservation.getReservationTotalPrice())
                .build();
    }
}
