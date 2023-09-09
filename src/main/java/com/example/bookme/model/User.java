package com.example.bookme.model;

import com.example.bookme.model.enumerations.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String mobilePhone;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "propertyUser")
    @JsonIgnore
    private List<Property> propertyList;
    @OneToMany(mappedBy = "reservationUser")
    @JsonIgnore
    private List<Reservation> reservationList;
    @ManyToMany
    @JsonIgnore
    private List<Property> favouriteList;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}

// {
//    "email": "jovan@hotmail.com",
//    "password": "123Jovan"
//}

//{
//    "propertyName": "jovan@hotmail.com",
//    "propertyDescription": "123Jovan",
//    "propertyCity" : "Skopje",
//    "propertyLocation" : "Skopje",
//    "propertyType" : "HOTEL",
//    "propertySize" : 15,
//    "propertyPrice" : 10,
//    "propertyImage" : "testImage",
//    "propertyUser" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3ZhbkBob3RtYWlsLmNvbSIsImlhdCI6MTY5MjkwODk5NiwiZXhwIjoxNjkyOTEwNDM2fQ._JnxJ-uscRz3fYXLEAqP_eBMwypmZplBp0QsbkW8e0Y"
//}

//{
//    "propertyName": "nikola@hotmail.com",
//    "propertyDescription": "123Nikola",
//    "propertyCity": "Ohrid",
//    "propertyLocation": "Ohrid",
//    "propertyType": "HOTEL",
//    "propertySize": 33,
//    "propertyPrice": 123
//}

//{
//        "reservationProperty": 5,
//        "reservationStartDate":"2023-08-02T22:00:00.000Z",
//        "reservationEndDate":"2023-08-03T22:00:00.000Z",
//        "reservationNumberOfPeople": 1,
//        "reservationTotalPrice": 5
//}

//{
//        "firstName": "Jovan",
//        "lastName":"Stojcev",
//        "mobilePhone":"078702604",
//        "email": "stojcevjovan@hotmail.com",
//        "password": "jovance123"
//}

//{
//        "userRating": 1,
//        "userComment":"2023-09-05T22:00:00.000Z",
//        "reservationStartDate":"2023-09-05T22:00:00.000Z",
//}