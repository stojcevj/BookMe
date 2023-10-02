package com.example.bookme.service;

import com.example.bookme.model.Property;
import com.example.bookme.model.Rating;
import com.example.bookme.model.User;
import com.example.bookme.model.dtos.RatingDto;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RatingService {
    Optional<Rating> addRatingToProperty(Long id,
                                         Authentication authentication,
                                         RatingDto ratingDto);
    Optional<Rating> findRatingByPropertyAndUser(Authentication authentication,
                                                 Long reservationId);
}
