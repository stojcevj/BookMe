package com.example.bookme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class RecentlyViewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User user;
    @ManyToOne
    private Property property;
    private LocalDateTime viewedAt;
    public RecentlyViewed(User user, Property property) {
        this.user = user;
        this.property = property;
        this.viewedAt = LocalDateTime.now();
    }
    public void updateDateTime(){
        viewedAt = LocalDateTime.now();
    }
}
