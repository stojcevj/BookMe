package com.example.bookme.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAddDto {
    private Long reservationProperty;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private Integer reservationNumberOfPeople;
    private Double reservationTotalPrice;
}
