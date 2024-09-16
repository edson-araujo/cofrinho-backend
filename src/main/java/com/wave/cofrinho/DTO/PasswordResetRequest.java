package com.wave.cofrinho.DTO;

import static com.wave.cofrinho.constants.ErrorMessage.PASSWORD2_CHARACTER_LENGTH;
import static com.wave.cofrinho.constants.ErrorMessage.PASSWORD_CHARACTER_LENGTH;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class PasswordResetRequest {

    private String email;

    @Size(min = 6, max = 16, message = PASSWORD_CHARACTER_LENGTH)
    private String password;

    @Size(min = 6, max = 16, message = PASSWORD2_CHARACTER_LENGTH)
    private String password2;
}
