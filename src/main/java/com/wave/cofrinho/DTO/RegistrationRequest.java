package com.wave.cofrinho.DTO;

import static com.wave.cofrinho.constants.ErrorMessage.EMAIL_CANNOT_BE_EMPTY;
import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_FIRST_NAME;
import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_LAST_NAME;
import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_NICKNAME;
import static com.wave.cofrinho.constants.ErrorMessage.INCORRECT_EMAIL;
import static com.wave.cofrinho.constants.ErrorMessage.PASSWORD2_CHARACTER_LENGTH;
import static com.wave.cofrinho.constants.ErrorMessage.PASSWORD_CHARACTER_LENGTH;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegistrationRequest {

    @NotBlank(message = "Fill captcha.")
    private String captcha;

    @NotBlank(message = EMPTY_FIRST_NAME)
    private String firstname;

    @NotBlank(message = EMPTY_NICKNAME)
    private String nickname;

    @NotBlank(message = EMPTY_LAST_NAME)
    private String lastname;

    @Size(min = 6, max = 16, message = PASSWORD_CHARACTER_LENGTH)
    private String password;

    @Size(min = 6, max = 16, message = PASSWORD2_CHARACTER_LENGTH)
    private String password2;

    @Email(message = INCORRECT_EMAIL)
    @NotBlank(message = EMAIL_CANNOT_BE_EMPTY)
    private String email;
}
