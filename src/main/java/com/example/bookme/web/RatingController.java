package com.example.bookme.web;

import com.example.bookme.model.Rating;
import com.example.bookme.model.dtos.RatingDto;
import com.example.bookme.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/rating")
@AllArgsConstructor
@CrossOrigin("*")
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
    @GetMapping("/check/{id}")
    public ResponseEntity<?> checkRatingForProperty(@PathVariable Long id,
                                                    Authentication authentication){
        try{
            Optional<Rating> rating = ratingService.findRatingByPropertyAndUser(authentication, id);
            if(rating.isPresent()){
                return ResponseEntity.ok().body(rating.get());
            }
            return ResponseEntity.ok().body(null);
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
