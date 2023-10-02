package com.example.bookme.service.impl;

import com.example.bookme.model.Property;
import com.example.bookme.model.Rating;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import com.example.bookme.model.dtos.RatingDto;
import com.example.bookme.model.exceptions.*;
import com.example.bookme.repository.PropertyRepository;
import com.example.bookme.repository.RatingRepository;
import com.example.bookme.repository.ReservationRepository;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final ReservationRepository reservationRepository;
    @Override
    public Optional<Rating> addRatingToProperty(Long id,
                                                Authentication authentication,
                                                RatingDto ratingDto) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Property propertyToBeRated = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        Reservation reservation = reservationRepository
                .findAllByReservationUserAndReservationPropertyAndReservationStartDateAndReservationEndDateIsBefore(loggedInUser, propertyToBeRated, ratingDto.getReservationStartDate(), LocalDateTime.now())
                .orElseThrow(ReservationNotFoundException::new);

        if(reservation != null){
            Rating checkRating = ratingRepository.findByPropertyRatedAndRatedBy(propertyToBeRated, loggedInUser)
                    .orElse(null);

            if(checkRating != null){
                checkRating.setUserRating(ratingDto.getUserRating());
                checkRating.setUserComment(ratingDto.getUserComment());
                checkRating.setRatingTime(LocalDateTime.now());
                return Optional.of(ratingRepository.save(checkRating));

            }

            Rating rating = new Rating(
                    loggedInUser,
                    propertyToBeRated,
                    ratingDto.getUserRating(),
                    ratingDto.getUserComment()
            );

            ratingRepository.save(rating);

            return Optional.of(rating);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rating> findRatingByPropertyAndUser(Authentication authentication,
                                                        Long reservationId) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        User reservationUser = userRepository.findByEmail(reservation.getReservationUser().getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(reservationUser != loggedInUser){
            throw new UserNotMatchingException();
        }

        Property reservationProperty = propertyRepository.findById(reservation.getReservationProperty().getId())
                .orElseThrow(PropertyNotFoundException::new);

        return ratingRepository.findByPropertyRatedAndRatedBy(reservationProperty, reservationUser);
    }
}
