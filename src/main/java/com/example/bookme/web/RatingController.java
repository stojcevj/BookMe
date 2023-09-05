package com.example.bookme.web;

import com.example.bookme.model.User;
import com.example.bookme.model.dto.RatingDto;
import com.example.bookme.service.RatingService;
import com.example.bookme.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    @PostMapping("/{id}")
    public ResponseEntity<?> addRatingToProperty(@PathVariable Long id,
                                                 Authentication authentication,
                                                 @RequestBody RatingDto ratingDto){
        try{
            return ratingService.addRatingToProperty(id,authentication, ratingDto)
                    .map(rating -> ResponseEntity.ok().body(rating))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch(Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
