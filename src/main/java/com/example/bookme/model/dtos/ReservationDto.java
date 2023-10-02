package com.example.bookme.model.dtos;

import com.example.bookme.model.Reservation;
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
