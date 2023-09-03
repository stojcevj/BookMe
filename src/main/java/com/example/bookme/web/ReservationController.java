package com.example.bookme.web;

import com.example.bookme.model.Reservation;
import com.example.bookme.model.dto.ReservationAddDto;
import com.example.bookme.repository.ReservationRepository;
import com.example.bookme.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    @PostMapping("/add")
    public ResponseEntity<?> addReservation(Authentication authentication,
                                            @RequestBody ReservationAddDto reservationAddDto){
        try{
            return reservationService.addReservation(authentication, reservationAddDto)
                    .map(reservation -> ResponseEntity.ok().body(reservation))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch(Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @DeleteMapping ("/{id}/delete")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id,
                                               Authentication authentication){
        try{
            return reservationService.deleteReservation(id,authentication)
                    .map(reservation -> ResponseEntity.ok().body(reservation))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @GetMapping
    public List<Reservation> getAllReservations(){
        return reservationService.findAll();
    }
}
