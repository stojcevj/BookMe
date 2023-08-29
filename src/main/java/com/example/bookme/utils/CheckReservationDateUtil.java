package com.example.bookme.utils;

import com.example.bookme.model.Reservation;
import com.example.bookme.model.dto.ReservationAddDto;

public class CheckReservationDateUtil {
    public static boolean CheckReservationDates(ReservationAddDto reservationAddDto,
                                                Reservation reservation){
        return ((reservationAddDto.getReservationStartDate().isAfter(reservation.getReservationStartDate())  &&
                reservationAddDto.getReservationStartDate().isAfter(reservation.getReservationEndDate())  &&
                reservationAddDto.getReservationEndDate().isAfter(reservation.getReservationStartDate())  &&
                reservationAddDto.getReservationEndDate().isAfter(reservation.getReservationEndDate()))  ||
                (reservationAddDto.getReservationStartDate().isBefore(reservation.getReservationStartDate())  &&
                reservationAddDto.getReservationStartDate().isBefore(reservation.getReservationEndDate())  &&
                reservationAddDto.getReservationEndDate().isBefore(reservation.getReservationStartDate())  &&
                reservationAddDto.getReservationEndDate().isBefore(reservation.getReservationEndDate())));
    }
}
