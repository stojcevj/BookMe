package com.example.bookme.service.impl;

import com.example.bookme.config.FileSaveConstants;
import com.example.bookme.model.*;
import com.example.bookme.model.dtos.PropertyDto;
import com.example.bookme.model.dtos.PropertySaveDto;
import com.example.bookme.model.dtos.PropertyEditDto;
import com.example.bookme.model.enumerations.PropertyType;
import com.example.bookme.model.exceptions.AnonymousUserException;
import com.example.bookme.model.exceptions.PropertyNotFoundException;
import com.example.bookme.model.exceptions.UserNotFoundException;
import com.example.bookme.model.exceptions.UserNotMatchingException;
import com.example.bookme.model.projections.PropertyEditProjection;
import com.example.bookme.model.projections.PropertyProjection;
import com.example.bookme.repository.*;
import com.example.bookme.service.PropertyService;
import com.example.bookme.utils.FileUploadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final RatingRepository ratingRepository;

    @Override
    public Page<PropertyProjection> findAll(String searchString,
                                            LocalDateTime startDate,
                                            LocalDateTime endDate,
                                            String propertyTypes,
                                            String propertyAmenities,
                                            String propertyRating,
                                            String priceRange,
                                            Pageable pageable,
                                            Authentication authentication) {
        int propertyAmenitiesCount = 0;
        int [] priceRangeArr = new int[2];

        if(startDate == null && endDate == null){
            startDate = LocalDateTime.now().plusYears(10);
            endDate = LocalDateTime.now().plusYears(10).plusDays(1);
        }

        if(propertyAmenities != null){
            propertyAmenities = propertyAmenities.replace(';', '-');
            propertyAmenitiesCount = propertyAmenities.split("-").length;
        }

        if(propertyRating != null){
            propertyRating = propertyRating.replace(';','-');
        }

        if(propertyTypes != null){
            propertyTypes = propertyTypes.replace(';','-');
        }

        if(priceRange != null){
            priceRangeArr[0] = Integer.parseInt(priceRange.split(";")[0]);
            priceRangeArr[1] = Integer.parseInt(priceRange.split(";")[1]);
        }else{
            priceRangeArr[1] = 999999;
        }

        Page<PropertyProjection> properties = propertyRepository.findAllPropertiesByFilter(pageable,
                propertyAmenities,
                propertyAmenitiesCount,
                propertyTypes,
                propertyRating,
                searchString,
                priceRangeArr[0],
                priceRangeArr[1],
                startDate,
                endDate);

        AtomicReference<Integer> numberOfRatings = new AtomicReference<>(0);
        return new PageImpl<>(properties.stream()
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
                        if(authentication == null){
                            return Boolean.FALSE;
                        }
                        return propertyIsBookmarkedByUser(authentication, property.getId());
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
                .collect(Collectors.toList()), pageable, properties.getTotalElements());
    }

    @Override
    public boolean propertyIsBookmarkedByUser(Authentication authentication,
                                              Long id){
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Property property = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        return loggedInUser.getFavouriteList().contains(property);
    }

    @Override
    public Page<PropertyProjection> findAllForUser(Authentication authentication,
                                         Pageable pageable) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        return propertyRepository.findAllByPropertyUser(loggedInUser.getId(), pageable);
    }

    @Override
    public Page<Property> findAllFavouritesForUser(Authentication authentication, Pageable pageable) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        loggedInUser.getFavouriteList().forEach(i -> i.setBookmarked(true));

        return new PageImpl<>(loggedInUser.getFavouriteList(), pageable, loggedInUser.getFavouriteList().size());
    }
    @Override
    public Optional<PropertyDto> findById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);
        PropertyDto propertyDto = PropertyDto.of(property);
        return Optional.of(propertyDto);
    }

    @Override
    public Optional<Property> save(PropertySaveDto propertySaveDto, Authentication authentication) throws IOException {
        User propertyUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        StringBuilder propertyImages = new StringBuilder();

        for(MultipartFile image : propertySaveDto.getImages()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            propertyImages.append(fileName).append(";");
        }

        Property property = new Property(
                propertySaveDto.getPropertyName(),
                propertySaveDto.getPropertyDescription(),
                propertySaveDto.getPropertyCity(),
                propertySaveDto.getPropertyLocation(),
                propertySaveDto.getPropertyAddress(),
                propertySaveDto.getPropertyType(),
                propertySaveDto.getPropertySize(),
                propertySaveDto.getPropertyPrice(),
                propertySaveDto.getPropertyImage(),
                propertyImages.toString(),
                propertyUser,
                propertySaveDto.getPropertyAmenities());

        Property savedProperty = propertyRepository.save(property);

        for(MultipartFile image : propertySaveDto.getImages()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uploadDir = FileSaveConstants.uploadDir + "/"+ savedProperty.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        }

        return Optional.of(property);
    }

    @Override
    public Optional<Property> edit(Authentication authentication,
                                   Long id,
                                   PropertyEditDto propertyDto) throws JsonProcessingException {
        Property propertyToEdit = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        User propertyUser = propertyToEdit.getPropertyUser();

        if(authentication == null){
            throw new AnonymousUserException();
        }

        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if(propertyUser == loggedInUser){
            propertyToEdit.setPropertyName(propertyDto.getPropertyName());
            propertyToEdit.setPropertyDescription(propertyDto.getPropertyDescription());
            propertyToEdit.setPropertyCity(propertyDto.getPropertyCity());
            propertyToEdit.setPropertyAddress(propertyDto.getPropertyAddress());
            propertyToEdit.setPropertyLocation(propertyDto.getPropertyLocation());
            propertyToEdit.setPropertyType(PropertyType.valueOf(propertyDto.getPropertyType()));
            propertyToEdit.setPropertySize(propertyDto.getPropertySize());
            propertyToEdit.setPropertyPrice(propertyDto.getPropertyPrice());
            propertyToEdit.setPropertyAmenities(propertyDto.getPropertyAmenities());

            return Optional.of(propertyRepository.save(propertyToEdit));
        }

        throw new UserNotMatchingException();
    }

    @Override
    public Optional<PropertyEditProjection> getEditDetails(Long id,
                                                           Authentication authentication) {
        PropertyEditProjection propertyToEdit = propertyRepository.findPropertyByIdForEdit(id)
                .orElseThrow(PropertyNotFoundException::new);

        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if(!Objects.equals(propertyToEdit.getProperty_user_id(), loggedInUser.getId())) {
            throw new UserNotMatchingException();
        }

        return Optional.of(propertyToEdit);
    }

    @Override
    public Optional<Property> deleteById(Authentication authentication,
                                         Long id) {
        Property propertyToDelete = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if(propertyToDelete.getPropertyUser() != loggedInUser){
            throw new UserNotMatchingException();
        }

        userRepository.findAll()
                        .forEach(user -> {
                            List<Property> favourites = user.getFavouriteList();
                            favourites.removeIf(i -> Objects.equals(i.getId(), propertyToDelete.getId()));
                            user.setFavouriteList(favourites);
                            userRepository.save(user);
                        });

        propertyRepository.delete(propertyToDelete);
        return Optional.of(propertyToDelete);
    }

    @Override
    public Optional<Property> addPropertyToFavourites(Authentication authentication, Long id) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Property propertyToAdd = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        List<Property> favourites = loggedInUser.getFavouriteList();
        favourites.add(propertyToAdd);

        loggedInUser.setFavouriteList(favourites);

        userRepository.save(loggedInUser);

        return Optional.of(propertyToAdd);
    }

    @Override
    public Optional<Property> deletePropertyFromFavourites(Authentication authentication, Long id) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Property propertyToDelete = propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new);

        List<Property> newFavourites = loggedInUser.getFavouriteList();
        newFavourites.remove(propertyToDelete);

        loggedInUser.setFavouriteList(newFavourites);

        userRepository.save(loggedInUser);

        return Optional.of(propertyToDelete);
    }
}
