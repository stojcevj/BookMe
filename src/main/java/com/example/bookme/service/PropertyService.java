package com.example.bookme.service;

import com.example.bookme.model.Property;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PropertyService {
    Page<Property> findAllWithPagination(Pageable pageable);
    Page<Property> findAllWithCitySearch(String search, Pageable pageable);
    Page<Property> findAll(Pageable pageable, String search, LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);
    Page<Property> findAllWithFreeReservationDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Property> findAllWithFreeReservationDatesAndCitySearch(LocalDateTime startDate, LocalDateTime endDate, String search, Pageable pageable);
    Page<Property> findAllForUser(Authentication authentication, Pageable pageable);
    Page<Property> findAllFavouritesForUser(Authentication authentication, Pageable pageable);
    Optional<Property> findById(Long id);
    Optional<Property> save(PropertyDto propertyDto) throws IOException;
    Optional<Property> edit(Authentication authentication, Long id, PropertyEditDto propertyDto) throws JsonProcessingException;
    Optional<Property> deleteById(Authentication authentication, Long id);
    Optional<Property> addPropertyToFavourites(Authentication authentication, Long id);
    boolean propertyIsBookmarkedByUser(Authentication authentication, Long id);
    Optional<Property> deletePropertyFromFavourites(Authentication authentication, Long id);
}
