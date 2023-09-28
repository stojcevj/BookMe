package com.example.bookme.service;

import com.example.bookme.model.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ContactMessageService {
    Page<ContactMessage> getAll(Pageable pageable);
    Optional<ContactMessage> save(ContactMessage contactMessage);
    boolean delete(Long id);
}
