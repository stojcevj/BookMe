package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.Rating;
import com.example.bookme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT * FROM rating r WHERE r.property_rated_id = :propertyId", nativeQuery = true)
    List<Rating> findAllByPropertyRated(@Param("propertyId") Long propertyId);
}
