package com.example.bookme.web;


import com.example.bookme.config.PageableConstants;
import com.example.bookme.model.Property;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.example.bookme.service.PropertyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/properties")
@AllArgsConstructor
@CrossOrigin("*")
public class PropertyController {
    private final PropertyService propertyService;

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllPropertiesForUser(Authentication authentication,
                                               @PageableDefault(size = PageableConstants.PAGE_SIZE, page = PageableConstants.DEFAULT_PAGE) Pageable pageable){
        try{
            return ResponseEntity.ok().body(propertyService.findAllForUser(authentication, pageable).getContent());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping
    public Page<Property> getAll(Authentication authentication,
                                                             @RequestParam(required = false, name = "s") String searchString,
                                                             @RequestParam(required = false,name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                             @RequestParam(required = false,name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                             @PageableDefault(size = PageableConstants.PAGE_SIZE, page = PageableConstants.DEFAULT_PAGE) Pageable pageable){
        return propertyService.findAll(pageable, searchString, startDate, endDate, authentication);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> save(Authentication authentication,
                                  PropertyDto propertyDto) throws IOException {
        try {
            if (authentication != null) {
                String email = authentication.getName();
                propertyDto.setPropertyUser(email);

                return propertyService.save(propertyDto)
                        .map(property -> ResponseEntity.ok().body(property))
                        .orElseGet(() -> ResponseEntity.badRequest().build());
            }
        }catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

        return ResponseEntity.status(403).build();
    }
    @PutMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/favourite}")
    public ResponseEntity<?> addPropertyToFavourites(Authentication authentication,
                                                     @PathVariable Long id){
        try{
            return propertyService.addPropertyToFavourites(authentication, id)
                    .map((property -> ResponseEntity.ok().body(property)))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/favourite/delete")
    public ResponseEntity<?> deletePropertyFromFavourites(Authentication authentication,
                                                          @PathVariable Long id){
        try{
            return propertyService.deletePropertyFromFavourites(authentication, id)
                    .map((property -> ResponseEntity.ok().body(property)))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favourites")
    public Page<Property> getFavouritesForUser(Authentication authentication,
                                               @PageableDefault(size = PageableConstants.PAGE_SIZE, page = PageableConstants.DEFAULT_PAGE) Pageable pageable){
        return propertyService.findAllFavouritesForUser(authentication, pageable);
    }
}