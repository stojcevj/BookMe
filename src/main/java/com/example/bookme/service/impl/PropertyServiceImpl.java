package com.example.bookme.service.impl;

import com.example.bookme.config.FileSaveConstants;
import com.example.bookme.model.Property;
import com.example.bookme.model.User;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.example.bookme.model.enumertaion.PropertyType;
import com.example.bookme.model.exceptions.AnonymousUserException;
import com.example.bookme.model.exceptions.PropertyNotFoundException;
import com.example.bookme.model.exceptions.UserNotFoundException;
import com.example.bookme.model.exceptions.UserNotMatchingException;
import com.example.bookme.repository.PropertyRepository;
import com.example.bookme.repository.ReservationRepository;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.PropertyService;
import com.example.bookme.utils.FileUploadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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


@AllArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Page<Property> findAllWithPagination(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    @Override
    public Page<Property> findAllWithCitySearch(String search,
                                                Pageable pageable) {
        if (search.isEmpty()){
            return propertyRepository.findAll(pageable);
        }

        return propertyRepository.findAllByPropertyCityContainingIgnoreCase(search, pageable);
    }

    @Override
    public Page<Property> findAllWithFreeReservationDates(LocalDateTime startDate,
                                                          LocalDateTime endDate,
                                                          Pageable pageable) {
        List<Long> usedProperties = reservationRepository
                .findAllByReservationStartDateGreaterThanAndReservationStartDateLessThanOrReservationEndDateGreaterThanAndReservationEndDateLessThan(startDate,endDate,startDate,endDate)
                .stream()
                .map(s -> s.getReservationProperty().getId())
                .toList();

        if(usedProperties.isEmpty()){
            return propertyRepository.findAll(pageable);
        }

        return propertyRepository.findAllByIdNotIn(usedProperties, pageable);
    }

    @Override
    public Page<Property> findAllWithFreeReservationDatesAndCitySearch(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       String search,
                                                                       Pageable pageable) {
        List<Long> usedProperties = reservationRepository
                .findAllByReservationProperty_PropertyCityLikeIgnoreCaseAndReservationStartDateGreaterThanAndReservationStartDateLessThanOrReservationEndDateGreaterThanAndReservationEndDateLessThan(search,startDate,endDate,startDate,endDate)
                .stream()
                .map(s -> s.getReservationProperty().getId())
                .toList();

        if(usedProperties.isEmpty()){
            return propertyRepository.findAllByPropertyCityContainingIgnoreCase(search, pageable);
        }

        return propertyRepository.findAllByIdNotIn(usedProperties, pageable);
    }

    @Override
    public Page<Property> findAllForUser(Authentication authentication,
                                         Pageable pageable) {
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        return propertyRepository.findAllByPropertyUser(loggedInUser, pageable);
    }

    @Override
    public Optional<Property> findById(Long id) {
        return Optional.ofNullable(propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new));
    }

    @Override
    public Optional<Property> save(PropertyDto propertyDto) throws IOException {
        User propertyUser = userRepository.findByEmail(propertyDto.getPropertyUser())
                .orElseThrow(UserNotFoundException::new);

        StringBuilder propertyImages = new StringBuilder();

        for(MultipartFile image : propertyDto.getImages()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            propertyImages.append(fileName).append(";");
        }

        Property property = new Property(
                propertyDto.getPropertyName(),
                propertyDto.getPropertyDescription(),
                propertyDto.getPropertyCity(),
                propertyDto.getPropertyLocation(),
                propertyDto.getPropertyAddress(),
                propertyDto.getPropertyType(),
                propertyDto.getPropertySize(),
                propertyDto.getPropertyPrice(),
                propertyDto.getPropertyImage(),
                propertyImages.toString(),
                propertyUser);

        Property savedProperty = propertyRepository.save(property);

        for(MultipartFile image : propertyDto.getImages()){
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
            propertyToEdit.setPropertyLocation(propertyDto.getPropertyLocation());
            propertyToEdit.setPropertyType(PropertyType.valueOf(propertyDto.getPropertyType()));
            propertyToEdit.setPropertySize(propertyDto.getPropertySize());
            propertyToEdit.setPropertyPrice(propertyDto.getPropertyPrice());

            return Optional.of(propertyRepository.save(propertyToEdit));
        }

        throw new UserNotMatchingException();
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
