package com.example.bookme.model;

import com.example.bookme.model.enumertaion.PropertyType;
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
    @OneToMany(mappedBy = "reservationProperty")
    @JsonIgnore
    private List<Reservation> reservationList;
    private String propertyImages;

    public Property(String propertyName, String propertyDescription, String propertyCity,
                    String propertyLocation, String propertyAddress, String propertyType, Integer propertySize,
                    Double propertyPrice, String propertyImage, String propertyImages, User propertyUser) {
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
    }
}

