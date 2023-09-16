package com.example.bookme.model.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface PropertyEditProjection {
    @JsonProperty("propertyName")
    String getProperty_name();
    @JsonProperty("propertyDescription")
    String getProperty_description();
    @JsonProperty("propertyCity")
    String getProperty_city();
    @JsonProperty("propertyAddress")
    String getProperty_address();
    @JsonProperty("propertyLocation")
    String getProperty_location();
    @JsonProperty("propertyType")
    String getProperty_type();
    @JsonProperty("propertySize")
    Integer getProperty_size();
    @JsonProperty("propertyPrice")
    Double getProperty_price();
    @JsonProperty("propertyAmenities")
    String getProperty_amenities();
    @JsonProperty("propertyUser")
    Long getProperty_user_id();
}
