package com.example.bookme.utils;

import com.example.bookme.model.Rating;
import com.example.bookme.model.projections.PropertyProjection;
import com.example.bookme.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ReCreatePropertyProjectionUtil {
    public static List<PropertyProjection> reCreate(Collection<PropertyProjection> properties, RatingRepository ratingRepository){
        AtomicReference<Integer> numberOfRatings = new AtomicReference<>(0);
        return properties.stream()
                .map(property -> new PropertyProjection() {
                    @Override
                    public Long getId() {
                        return property.getId();
                    }

                    @Override
                    public String getProperty_name() {
                        return property.getProperty_name();
                    }

                    @Override
                    public String getProperty_description() {
                        return property.getProperty_description();
                    }

                    @Override
                    public String getProperty_city() {
                        return property.getProperty_city();
                    }

                    @Override
                    public String getProperty_address() {
                        return property.getProperty_address();
                    }

                    @Override
                    public String getProperty_location() {
                        return property.getProperty_location();
                    }

                    @Override
                    public String getProperty_type() {
                        return property.getProperty_type();
                    }

                    @Override
                    public Integer getProperty_size() {
                        return property.getProperty_size();
                    }

                    @Override
                    public Double getProperty_price() {
                        return property.getProperty_price();
                    }

                    @Override
                    public String getProperty_image() {
                        return property.getProperty_image();
                    }

                    @Override
                    public String getProperty_images() {
                        return property.getProperty_images();
                    }

                    @Override
                    public Boolean getBookmarked() {
                        return false;
                    }

                    @Override
                    public Double getAverageRating() {
                        AtomicReference<Double> avgRating = new AtomicReference<>(0D);

                        ratingRepository.findAllByPropertyRated(property.getId())
                                .stream()
                                .mapToDouble(Rating::getUserRating)
                                .forEach(i -> {
                                    numberOfRatings.getAndSet(numberOfRatings.get() + 1);
                                    avgRating.updateAndGet(v -> v + i);
                                });

                        if (avgRating.get() == 0.0 || numberOfRatings.get() == 0) {
                            return 0D;
                        }

                        return avgRating.get() / numberOfRatings.get();
                    }

                    @Override
                    public Integer getNumberOfRatings() {
                        Integer numOfRatings = numberOfRatings.get();
                        numberOfRatings.set(0);
                        return numOfRatings;
                    }
                })
                .collect(Collectors.toList());
    }
}
