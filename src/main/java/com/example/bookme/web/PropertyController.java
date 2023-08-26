package com.example.bookme.web;


import com.example.bookme.model.Property;
import com.example.bookme.model.dto.PropertyDto;
import com.example.bookme.model.dto.PropertyEditDto;
import com.example.bookme.service.PropertyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@AllArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;
    @GetMapping("/")
    public List<Property> getAll(){
        return propertyService.findAll();
    }

    @GetMapping
    public List<Property> getAllWithPagination(Pageable pageable){
        return propertyService.findAllWithPagination(pageable).getContent();
    }

    @PostMapping
    public ResponseEntity<Property> save(Authentication authentication,
                                         @RequestPart("data") PropertyDto propertyDto,
                                         @RequestPart("images") MultipartFile [] images) throws IOException {
        if(authentication != null){
            String email = authentication.getName();
            propertyDto.setPropertyUser(email);

            return propertyService.save(propertyDto, images)
                    .map(property -> ResponseEntity.ok().body(property))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }

        return ResponseEntity.status(403).build();
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<Property> edit(@PathVariable Long id,
                                         Authentication authentication,
                                         @RequestBody PropertyEditDto propertyDto) throws JsonProcessingException {
        return propertyService.edit(authentication, id, propertyDto)
                .map(property -> ResponseEntity.ok().body(property))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}