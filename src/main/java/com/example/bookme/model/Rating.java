package com.example.bookme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne
    private User ratedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Property propertyRated;
    private Double userRating;
    private String userComment;
    private LocalDateTime ratingTime;
    public Rating(User ratedBy, Property propertyRated, Double userRating, String userComment) {
        this.ratedBy = ratedBy;
        this.propertyRated = propertyRated;
        this.userRating = userRating;
        this.userComment = userComment;
        this.ratingTime = LocalDateTime.now();
    }
}
