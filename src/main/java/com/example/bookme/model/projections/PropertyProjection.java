package com.example.bookme.model.projections;

import com.example.bookme.repository.PropertyRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.sql.In;

public interface PropertyProjection {
    Long getId();
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
    @JsonProperty("propertyImage")
    String getProperty_image();
    @JsonProperty("propertyImages")
    String getProperty_images();
    @JsonProperty("bookmarked")
    Boolean getBookmarked();
    @JsonProperty("propertyAverageRating")
    Double getAverageRating();
    @JsonProperty("propertyNumberOfRatings")
    Integer getNumberOfRatings();
}
