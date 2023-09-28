package com.example.bookme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Property reservationProperty;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private Integer reservationNumberOfPeople;
    private Double reservationTotalPrice;
    public Reservation(User reservationUser,
                       Property reservationProperty,
                       LocalDateTime reservationStartDate,
                       LocalDateTime reservationEndDate,
                       Integer reservationNumberOfPeople,
                       Double reservationTotalPrice) {
        this.reservationUser = reservationUser;
        this.reservationProperty = reservationProperty;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.reservationNumberOfPeople = reservationNumberOfPeople;
        this.reservationTotalPrice = reservationTotalPrice;
    }
}

