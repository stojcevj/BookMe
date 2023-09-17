package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.RecentlyViewed;
import com.example.bookme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long> {
    Optional<RecentlyViewed> findByUserAndProperty(User user, Property property);
    List<RecentlyViewed> findByUser(User user);
    Page<RecentlyViewed> findByUserOrderByViewedAtDesc(User user, Pageable pageable);
    List<RecentlyViewed> findAllByProperty(Property property);
}
