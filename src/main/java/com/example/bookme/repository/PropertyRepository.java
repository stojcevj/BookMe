package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.User;
import com.example.bookme.model.projections.PropertyEditProjection;
import com.example.bookme.model.projections.PropertyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAll(Pageable pageable);
    @Query(value = "SELECT p.id, p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_image, p.property_images FROM property p", nativeQuery = true)
    Page<PropertyProjection> findAllWithProjection(Pageable pageable);
    @Query(value = "SELECT p.id, p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_image, p.property_images FROM property p WHERE p.id NOT IN :ids", nativeQuery = true)
    Page<PropertyProjection> findAllByIdNotIn(@Param("ids") List<Long> ids, Pageable pageable);
    @Query(value = "SELECT p.id, p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_image, p.property_images FROM property p WHERE LOWER(p.property_city) LIKE LOWER(CONCAT('%', :city, '%'))", nativeQuery = true)
    Page<PropertyProjection> findAllByPropertyCityContainingIgnoreCase(@Param("city") String city, Pageable pageable);
    @Query(value = "SELECT p.id, p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_image, p.property_images FROM property p WHERE p.property_user_id = :userId", nativeQuery = true)
    Page<PropertyProjection> findAllByPropertyUser(@Param("userId") Long userId, Pageable pageable);
    @Query(value = "SELECT p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_amenities, p.property_user_id FROM property p WHERE p.id = :propertyId", nativeQuery = true)
    Optional<PropertyEditProjection> findPropertyByIdForEdit(@Param("propertyId") Long propertyId);
}
