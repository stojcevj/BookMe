package com.example.bookme.config;

import com.example.bookme.config.filters.JWTAuthenticationFilter;
import com.example.bookme.config.filters.JWTAuthorizationFilter;
import com.example.bookme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
public class JWTWebSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;
    private final UserService userService;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**","/api/v1/properties/{\\d+}")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), userService, passwordEncoder), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), userService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public JWTAuthorizationFilter authorizationFilter(HttpSecurity http) throws Exception {
        return new JWTAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), userService);
    }
    @Bean
    public JWTAuthenticationFilter authenticationFilter(HttpSecurity http) throws Exception {
        return new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), userService, passwordEncoder);
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

}


