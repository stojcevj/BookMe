package com.example.bookme.web;

import com.example.bookme.model.ContactMessage;
import com.example.bookme.service.ContactMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/help")
@AllArgsConstructor
@CrossOrigin("*")
public class HelpController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ContactMessage contactMessage){
        return this.contactMessageService.save(contactMessage)
                .map(i -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.badRequest().build());
    }
}
