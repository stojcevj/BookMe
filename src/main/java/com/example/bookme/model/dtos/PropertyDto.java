package com.example.bookme.model.dtos;

import com.example.bookme.model.Property;
import com.example.bookme.model.Rating;
import com.example.bookme.model.Reservation;
import com.example.bookme.model.User;
import com.example.bookme.model.enumerations.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyDto {
    private Long id;
    private String propertyName;
    private String propertyDescription;
    private String propertyCity;
    private String propertyAddress;
    private String propertyLocation;
    private PropertyType propertyType;
    private Integer propertySize;
    private Double propertyPrice;
    private String propertyImage;
    private User propertyUser;
    private List<Reservation> reservationList;
    private List<Rating> propertyRating;
    private String propertyImages;
    private String propertyAmenities;
    private boolean bookmarked;
    private Double averageRating;

    public Double getAverageRating(){
        if(propertyRating.isEmpty()){
            return 0d;
        }

        return propertyRating.stream()
                .mapToDouble(Rating::getUserRating)
                .average()
                .orElseThrow();
    }

    public static PropertyDto of(Property property){
        PropertyDto propertyDto = PropertyDto.builder()
                .id(property.getId())
                .propertyName(property.getPropertyName())
                .propertyDescription(property.getPropertyDescription())
                .propertyCity(property.getPropertyCity())
                .propertyAddress(property.getPropertyAddress())
                .propertyLocation(property.getPropertyLocation())
                .propertyType(property.getPropertyType())
                .propertySize(property.getPropertySize())
                .propertyPrice(property.getPropertyPrice())
                .propertyImage(property.getPropertyImage())
                .propertyUser(property.getPropertyUser())
                .propertyRating(property.getPropertyRating())
                .propertyImages(property.getPropertyImages())
                .propertyAmenities(property.getPropertyAmenities())
                .bookmarked(property.isBookmarked())
                .build();
        propertyDto.setAverageRating(propertyDto.getAverageRating());
        List<Reservation> propertyReservations = property.getReservationList()
                .stream()
                .filter(s -> s.getReservationStartDate().isAfter(LocalDateTime.now())
                        || s.getReservationEndDate().isAfter(LocalDateTime.now()))
                .toList();
        propertyDto.setReservationList(propertyReservations);

        return propertyDto;
    }
}
