package com.example.bookme.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User reservationUser;
    @ManyToOne
    private Property reservationProperty;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private Integer reservationNumberOfPeople;
    private Double reservationTotalPrice;
}

