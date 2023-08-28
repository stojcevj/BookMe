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
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@AllArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    @Override
    public Page<Property> findAllWithPagination(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    @Override
    public Optional<Property> findById(Long id) {
        return Optional.ofNullable(propertyRepository.findById(id)
                .orElseThrow(PropertyNotFoundException::new));
    }

    @Override
    public Optional<Property> findByName(String name) {
        return Optional.ofNullable(propertyRepository.findByPropertyName(name)
                .orElseThrow(PropertyNotFoundException::new));
    }

    @Override
    public Optional<Property> save(PropertyDto propertyDto, MultipartFile[] images) throws IOException {
        User propertyUser = userRepository.findByEmail(propertyDto.getPropertyUser())
                .orElseThrow(UserNotFoundException::new);

        StringBuilder propertyImages = new StringBuilder();

        for(MultipartFile image : images){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            propertyImages.append(fileName).append(";");
        }

        Property property = new Property(propertyDto.getPropertyName(),
                propertyDto.getPropertyDescription(),
                propertyDto.getPropertyCity(),
                propertyDto.getPropertyAddress(),
                propertyDto.getPropertyLocation(),
                propertyDto.getPropertyType(),
                propertyDto.getPropertySize(),
                propertyDto.getPropertyPrice(),
                propertyDto.getPropertyImage(),
                propertyImages.toString(),
                propertyUser);

        Property savedProperty = propertyRepository.save(property);

        for(MultipartFile image : images){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uploadDir = FileSaveConstants.uploadDir + "/"+ savedProperty.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        }

        return Optional.of(property);
    }

    @Override
    public Optional<Property> edit(Authentication authentication, Long id, PropertyEditDto propertyDto) throws JsonProcessingException {
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

        User authenticatedUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if(propertyToDelete.getPropertyUser() != authenticatedUser){
            throw new UserNotMatchingException();
        }

        propertyRepository.delete(propertyToDelete);
        return Optional.of(propertyToDelete);
    }
}
