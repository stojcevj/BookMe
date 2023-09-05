package com.example.bookme.service;

import com.example.bookme.model.Rating;
import com.example.bookme.model.dto.RatingDto;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RatingService {
    Optional<Rating> addRatingToProperty(Long id, Authentication authentication, RatingDto ratingDto);
}
