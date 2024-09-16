package com.wave.cofrinho.DTO.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import com.wave.cofrinho.Enum.Role;

@Getter
@Setter
public class BaseUserResponse {
    private Long id;
    private String email;
    private String firstname;
    private Set<Role> roles;
    private String provider;
}
