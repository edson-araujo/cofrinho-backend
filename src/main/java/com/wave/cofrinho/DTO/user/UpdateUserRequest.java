package com.wave.cofrinho.DTO.user;

import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_FIRST_NAME;
import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_LAST_NAME;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdateUserRequest {
    private Long id;

    @NotBlank(message = EMPTY_FIRST_NAME)
    private String firstName;

    @NotBlank(message = EMPTY_LAST_NAME)
    private String lastName;

    private String city;
    private String address;
    private String phoneNumber;
    private String postIndex;
}
