package com.example.bookme.service;

import com.example.bookme.model.Property;
import com.example.bookme.model.dtos.PropertyDto;
import com.example.bookme.model.dtos.PropertySaveDto;
import com.example.bookme.model.dtos.PropertyEditDto;
import com.example.bookme.model.projections.PropertyEditProjection;
import com.example.bookme.model.projections.PropertyProjection;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PropertyService {
    Page<PropertyProjection> findAllWithPagination(Pageable pageable);
    Page<PropertyProjection> findAllWithCitySearch(String search, Pageable pageable);
    Page<PropertyProjection> findAll(Pageable pageable, String search, LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);
    Page<PropertyProjection> findAllWithFreeReservationDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<PropertyProjection> findAllWithFreeReservationDatesAndCitySearch(LocalDateTime startDate, LocalDateTime endDate, String search, Pageable pageable);
    Page<PropertyProjection> findAllForUser(Authentication authentication, Pageable pageable);
    Page<Property> findAllFavouritesForUser(Authentication authentication, Pageable pageable);
    Optional<PropertyDto> findById(Long id);
    Optional<Property> save(PropertySaveDto propertySaveDto, Authentication authentication) throws IOException;
    Optional<Property> edit(Authentication authentication, Long id, PropertyEditDto propertyDto) throws JsonProcessingException;
    Optional<PropertyEditProjection> getEditDetails(Long id, Authentication authentication);
    Optional<Property> deleteById(Authentication authentication, Long id);
    Optional<Property> addPropertyToFavourites(Authentication authentication, Long id);
    boolean propertyIsBookmarkedByUser(Authentication authentication, Long id);
    Optional<Property> deletePropertyFromFavourites(Authentication authentication, Long id);
}
