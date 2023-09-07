package com.example.bookme.model.dto;

import com.example.bookme.model.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    Property property;
    boolean isBookmarked;

    public PropertyResponse(Property property) {
        this.property = property;
        this.isBookmarked = false;
    }
}
