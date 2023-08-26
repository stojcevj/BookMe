package com.example.bookme.repository;

import com.example.bookme.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByPropertyName(String name);
    Page<Property> findAll(Pageable pageable);
}
