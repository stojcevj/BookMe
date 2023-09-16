package com.example.bookme.model;

import com.example.bookme.model.enumerations.PropertyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String propertyName;
    @Column(columnDefinition = "TEXT")
    private String propertyDescription;
    private String propertyCity;
    private String propertyAddress;
    private String propertyLocation;
    @Enumerated(value = EnumType.STRING)
    private PropertyType propertyType;
    private Integer propertySize;
    private Double propertyPrice;
    private String propertyImage;
    @ManyToOne
    private User propertyUser;
    @OneToMany(mappedBy = "reservationProperty", cascade = CascadeType.ALL)
    private List<Reservation> reservationList;
    @OneToMany(mappedBy = "propertyRated", cascade = CascadeType.ALL)
    private List<Rating> propertyRating;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RecentlyViewed> propertyRecentlyViewed;
    private String propertyImages;
    private String propertyAmenities;
    @Transient
    private boolean bookmarked;

    public Property(String propertyName, String propertyDescription, String propertyCity,
                    String propertyLocation, String propertyAddress, String propertyType, Integer propertySize,
                    Double propertyPrice, String propertyImage, String propertyImages, User propertyUser, String propertyAmenities) {
        this.propertyName = propertyName;
        this.propertyDescription = propertyDescription;
        this.propertyCity = propertyCity;
        this.propertyLocation = propertyLocation;
        this.propertyAddress = propertyAddress;
        this.propertyType = PropertyType.valueOf(propertyType);
        this.propertySize = propertySize;
        this.propertyPrice = propertyPrice;
        this.propertyImage = propertyImage;
        this.propertyImages = propertyImages;
        this.propertyUser = propertyUser;
        this.propertyAmenities = propertyAmenities;
    }
}

