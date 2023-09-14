package com.example.bookme.service.impl;

import com.example.bookme.model.Property;
import com.example.bookme.model.RecentlyViewed;
import com.example.bookme.model.User;
import com.example.bookme.model.exceptions.PropertyNotFoundException;
import com.example.bookme.model.exceptions.RecentlyViewedNotFoundException;
import com.example.bookme.model.exceptions.UserNotFoundException;
import com.example.bookme.repository.PropertyRepository;
import com.example.bookme.repository.RecentlyViewedRepository;
import com.example.bookme.repository.UserRepository;
import com.example.bookme.service.RecentlyViewedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecentlyViewedServiceImpl implements RecentlyViewedService {

    private final RecentlyViewedRepository recentlyViewedRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;


    public RecentlyViewedServiceImpl(RecentlyViewedRepository recentlyViewedRepository,
                                     UserRepository userRepository,
                                     PropertyRepository propertyRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Page<RecentlyViewed> findAll(Authentication authentication, Pageable pageable) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return recentlyViewedRepository.findByUserOrderByViewedAtDesc(user, pageable);
    }

    @Override
    public Optional<RecentlyViewed> save(Authentication authentication, Long propertyId) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(PropertyNotFoundException::new);
        RecentlyViewed recentlyViewed = recentlyViewedRepository
                .findByUserAndProperty(user, property)
                .orElse(null);

        if(recentlyViewed != null){
            recentlyViewed.updateDateTime();
            recentlyViewedRepository.save(recentlyViewed);
            return Optional.of(recentlyViewed);
        }
        else{
            return Optional.of(recentlyViewedRepository.save(new RecentlyViewed(user, property)));
        }
    }

    @Override
    public boolean removeAll(Authentication authentication) {
      //  User user = userRepository.findByEmail(authentication.getName())
      //          .orElseThrow(UserNotFoundException::new);
      //  recentlyViewedRepository.deleteAll(recentlyViewedRepository.findByUser(user));
        return true;
    }

    @Override
    public boolean deleteById(Authentication authentication, Long id) {
        recentlyViewedRepository.deleteById(id);
        return true;
    }
}
