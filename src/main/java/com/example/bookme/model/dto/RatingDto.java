package com.example.bookme.model.dto;

import com.example.bookme.model.Property;
import com.example.bookme.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    private Double userRating;
    private String userComment;
    private LocalDateTime reservationStartDate;
}
