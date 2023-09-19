package com.example.bookme.model.dtos;

import com.example.bookme.model.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyEditDto {
    private String propertyName;
    private String propertyDescription;
    private String propertyCity;
    private String propertyAddress;
    private String propertyLocation;
    private String propertyType;
    private Integer propertySize;
    private Double propertyPrice;
    private String propertyAmenities;
}