package com.wave.cofrinho.DTO.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseUserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
}
