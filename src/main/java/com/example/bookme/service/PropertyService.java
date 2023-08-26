package com.example.bookme.service;

import com.example.bookme.model.Property;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PropertyService {
    List<Property> findAll();
    Page<Property> findAllWithPagination(Pageable pageable);
    Optional<Property> findById(Long id);
    Optional<Property> findByName(String name);
    Optional<Property> save(PropertyDto propertyDto, MultipartFile[] images) throws IOException;
    Optional<Property> edit(Authentication authentication, Long id, PropertyEditDto propertyDto) throws JsonProcessingException;
    void deleteById(Long id);
}
