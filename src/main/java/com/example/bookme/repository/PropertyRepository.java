package com.example.bookme.repository;

import com.example.bookme.model.Property;
import com.example.bookme.model.User;
import com.example.bookme.model.projections.PropertyEditProjection;
import com.example.bookme.model.projections.PropertyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAll(Pageable pageable);
    @Query(value = "SELECT p.id, p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_image, p.property_images FROM property p WHERE p.property_user_id = :userId", nativeQuery = true)
    Page<PropertyProjection> findAllByPropertyUser(@Param("userId") Long userId, Pageable pageable);
    @Query(value = "SELECT p.property_name, p.property_description, p.property_city, p.property_address, p.property_location, p.property_type, p.property_size, p.property_price, p.property_amenities, p.property_user_id FROM property p WHERE p.id = :propertyId", nativeQuery = true)
    Optional<PropertyEditProjection> findPropertyByIdForEdit(@Param("propertyId") Long propertyId);
    @Query(value = "SELECT \n" +
            "  p.id, \n" +
            "  p.property_name, \n" +
            "  p.property_description, \n" +
            "  p.property_city, \n" +
            "  p.property_address, \n" +
            "  p.property_location, \n" +
            "  p.property_type, \n" +
            "  p.property_size, \n" +
            "  p.property_price, \n" +
            "  p.property_image, \n" +
            "  p.property_images \n" +
            "FROM \n" +
            "  property p \n" +
            "WHERE \n" +
            "  (\n" +
            "    :propertyAmenities IS NULL \n" +
            "    OR (\n" +
            "      SELECT \n" +
            "        COUNT(*) \n" +
            "      FROM \n" +
            "        unnest(\n" +
            "          string_to_array(:propertyAmenities, '-')\n" +
            "        ) a \n" +
            "      WHERE \n" +
            "        p.property_amenities LIKE CONCAT('%', a, '%')\n" +
            "    ) = :propertyAmenitiesCount\n" +
            "  ) \n" +
            "  AND (\n" +
            "    :propertyTypes IS NULL \n" +
            "    OR (\n" +
            "      SELECT \n" +
            "        COUNT(*) \n" +
            "      FROM \n" +
            "        unnest(\n" +
            "          string_to_array(:propertyTypes, '-')\n" +
            "        ) b \n" +
            "      WHERE \n" +
            "        p.property_type LIKE CONCAT('%', b, '%')\n" +
            "    ) = 1\n" +
            "  ) \n" +
            "  AND (\n" +
            "    :propertyRating IS NULL \n" +
            "    OR EXISTS (\n" +
            "      SELECT \n" +
            "        1 \n" +
            "      FROM \n" +
            "        unnest(\n" +
            "          string_to_array(:propertyRating, '-')\n" +
            "        ) AS \n" +
            "      values \n" +
            "        (val) \n" +
            "      WHERE \n" +
            "        (\n" +
            "          SELECT \n" +
            "            AVG(r.user_rating) \n" +
            "          FROM \n" +
            "            rating r \n" +
            "          WHERE \n" +
            "            r.property_rated_id = p.id\n" +
            "        ) >= CAST(val as double precision)\n" +
            "    )\n" +
            "  ) \n" +
            "  AND LOWER(p.property_city) LIKE CONCAT(\n" +
            "    '%', \n" +
            "    LOWER(:search), \n" +
            "    '%'\n" +
            "  ) \n" +
            "  AND p.property_price >= :propertyStartPrice \n" +
            "  AND p.property_price <= :propertyEndPrice \n" +
            "  AND (\n" +
            "    SELECT \n" +
            "      COUNT(*) \n" +
            "    FROM \n" +
            "      reservation res \n" +
            "    WHERE \n" +
            "      res.reservation_property_id = p.id \n" +
            "      AND (\n" +
            "        (\n" +
            "          :propertyStartDate < :propertyEndDate \n" +
            "          AND :propertyStartDate > res.reservation_start_date \n" +
            "          AND :propertyStartDate >= res.reservation_end_date \n" +
            "          AND :propertyEndDate > res.reservation_start_date \n" +
            "          AND :propertyEndDate > res.reservation_end_date\n" +
            "        ) \n" +
            "        OR (\n" +
            "          :propertyStartDate < :propertyEndDate \n" +
            "          AND :propertyStartDate < res.reservation_start_date \n" +
            "          AND :propertyStartDate < res.reservation_end_date \n" +
            "          AND :propertyEndDate <= res.reservation_start_date \n" +
            "          AND :propertyEndDate < res.reservation_end_date\n" +
            "        )\n" +
            "      )\n" +
            "  ) = (\n" +
            "    SELECT \n" +
            "      COUNT(*) \n" +
            "    FROM \n" +
            "      reservation res \n" +
            "    WHERE \n" +
            "      res.reservation_property_id = p.id\n" +
            "  )\n", nativeQuery = true)
    Page<PropertyProjection> findAllPropertiesByFilter(Pageable pageable,
                                                       @Param("propertyAmenities") String propertyAmenities,
                                                       @Param("propertyAmenitiesCount") Integer propertyAmenitiesCount,
                                                       @Param("propertyTypes") String propertyTypes,
                                                       @Param("propertyRating") String propertyRating,
                                                       @Param("search") String search,
                                                       @Param("propertyStartPrice") Integer propertyStartPrice,
                                                       @Param("propertyEndPrice") Integer propertyEndPrice,
                                                       @Param("propertyStartDate") LocalDateTime propertyStartDate,
                                                       @Param("propertyEndDate") LocalDateTime propertyEndDate);
}
