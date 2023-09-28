package com.example.bookme.service.impl;

import com.example.bookme.model.ContactMessage;
import com.example.bookme.repository.ContactMessageRepository;
import com.example.bookme.service.ContactMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    @Override
    public Page<ContactMessage> getAll(Pageable pageable) {
        return this.contactMessageRepository.findAll(pageable);
    }

    @Override
    public Optional<ContactMessage> save(ContactMessage contactMessage) {
        return Optional.of(contactMessageRepository.save(contactMessage));
    }

    @Override
    public boolean delete(Long id) {
        if (contactMessageRepository.existsById(id)){
            contactMessageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
