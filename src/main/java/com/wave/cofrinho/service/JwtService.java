package com.wave.cofrinho.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.wave.cofrinho.model.User;

public interface JwtService {
    String extractUsername(String token);

    String generatedToken(UserDetails userDetails);

}
