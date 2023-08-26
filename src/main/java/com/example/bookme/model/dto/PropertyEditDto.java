package com.example.bookme.model.dto;

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
    private String propertyLocation;
    private String propertyType;
    private Integer propertySize;
    private Double propertyPrice;
}