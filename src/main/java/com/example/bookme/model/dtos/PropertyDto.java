package com.example.bookme.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {
    private String propertyName;
    private String propertyDescription;
    private String propertyCity;
    private String propertyAddress;
    private String propertyLocation;
    private String propertyType;
    private Integer propertySize;
    private Double propertyPrice;
    private String propertyImage;
    private String propertyImages;
    private String propertyUser;
    private MultipartFile [] images;
}
