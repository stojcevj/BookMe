package com.example.bookme.service.impl;

import com.example.bookme.model.Property;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import com.example.bookme.model.dto.ReservationAddDto;
import com.example.bookme.model.exceptions.*;
import com.example.bookme.repository.PropertyRepository;
import com.example.bookme.repository.ReservationRepository;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.ReservationService;
import com.example.bookme.service.UserService;
import com.example.bookme.utils.CheckReservationDateUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> addReservation(Authentication authentication,
                                                ReservationAddDto reservationAddDto) {
        User reservationUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Property reservationProperty = propertyRepository.findById(reservationAddDto.getReservationProperty())
                .orElseThrow(PropertyNotFoundException::new);

        List<Reservation> reservedOnTheSameDate = reservationRepository.findAllByReservationProperty(reservationProperty)
                .stream()
                .filter(s -> !CheckReservationDateUtil.CheckReservationDates(reservationAddDto, s))
                .toList();

        if(!reservedOnTheSameDate.isEmpty()){
            throw new ReservationDatesAreFulfilledException();
        }

        Reservation reservationToAdd = new Reservation(reservationUser,
                reservationProperty,
                reservationAddDto.getReservationStartDate(),
                reservationAddDto.getReservationEndDate(),
                reservationAddDto.getReservationNumberOfPeople(),
                reservationAddDto.getReservationTotalPrice());

        return Optional.of(reservationRepository.save(reservationToAdd));
    }

    @Override
    public Optional<Reservation> deleteReservation(Long id, Authentication authentication) {
        Reservation reservationToDelete = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        User authenticatedUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if(reservationToDelete.getReservationUser() != authenticatedUser){
            throw new UserNotMatchingException();
        }

        reservationRepository.delete(reservationToDelete);
        return Optional.of(reservationToDelete);
    }
}
