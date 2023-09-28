package com.example.bookme.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    @Column(columnDefinition = "TEXT")
    private String text;

    public ContactMessage(){}

    public ContactMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }
}
