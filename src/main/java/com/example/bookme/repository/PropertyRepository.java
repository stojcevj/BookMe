package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAll(Pageable pageable);
    Page<Property> findAllByIdNotIn(List<Long> ids, Pageable pageable);
    Page<Property> findAllByPropertyCityContainingIgnoreCase(String city, Pageable pageable);
    Page<Property> findAllByPropertyUser(User user, Pageable pageable);
}
