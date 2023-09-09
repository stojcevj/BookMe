package com.example.bookme.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    private Double userRating;
    private String userComment;
    private LocalDateTime reservationStartDate;
}
