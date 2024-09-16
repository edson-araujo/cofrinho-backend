package com.wave.cofrinho.DTO.auth;

import com.wave.cofrinho.DTO.user.UserResponse;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserResponse user;
    private String token;
    private boolean isActive;
}
