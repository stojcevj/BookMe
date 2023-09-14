package com.example.bookme.service;

import com.example.bookme.model.Property;
import com.example.bookme.model.RecentlyViewed;
import com.example.bookme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface RecentlyViewedService {
    Page<RecentlyViewed> findAll(Authentication authentication, Pageable pageable);
    Optional<RecentlyViewed> save(Authentication authentication, Long propertyId);
    boolean removeAll(Authentication authentication);
    boolean deleteById(Authentication authentication, Long id);
}
