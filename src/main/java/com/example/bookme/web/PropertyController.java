package com.example.bookme.web;


import com.example.bookme.model.Property;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.example.bookme.service.PropertyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@AllArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;
    @GetMapping
    public List<Property> getAllWithPagination(@RequestParam(required = false, name = "s") String searchString,
                                               @RequestParam(required = false,name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                               @RequestParam(required = false,name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                               @PageableDefault(size = 10, page = 0) Pageable pageable){
        if(searchString == null){
            return propertyService.findAllWithPagination(pageable).getContent();
        }
        if(!searchString.isEmpty() && (startDate == null || endDate == null)){
            return propertyService.findAllWithCitySearch(searchString, pageable).getContent();
        }
        if(!searchString.isEmpty()){
            return propertyService.findAllWithFreeReservationDatesAndCitySearch(startDate, endDate, searchString, pageable).getContent();
        }
        if(startDate != null && endDate != null){
            return propertyService.findAllWithFreeReservationDates(startDate, endDate, pageable).getContent();
        }
        return propertyService.findAllWithPagination(pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<?> save(Authentication authentication,
                                  PropertyDto propertyDto) throws IOException {
        try {
            if (authentication != null) {
                String email = authentication.getName();
                propertyDto.setPropertyUser(email);

                return propertyService.save(propertyDto, propertyDto.getImages())
                        .map(property -> ResponseEntity.ok().body(property))
                        .orElseGet(() -> ResponseEntity.badRequest().build());
            }
        }catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

        return ResponseEntity.status(403).build();
    }
    @PutMapping("/{id}/edit")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                         Authentication authentication,
                                         @RequestBody PropertyEditDto propertyDto) throws JsonProcessingException {
        try{
            return propertyService.edit(authentication, id, propertyDto)
                    .map(property -> ResponseEntity.ok().body(property))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    Authentication authentication){
        try{
            return propertyService.deleteById(authentication, id)
                    .map(property -> ResponseEntity.ok().body(property))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}