package com.wave.cofrinho.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.wave.cofrinho.DTO.PasswordResetRequest;
import com.wave.cofrinho.DTO.RegistrationRequest;
import com.wave.cofrinho.DTO.auth.AuthenticationRequest;
import com.wave.cofrinho.DTO.auth.AuthenticationResponse;
import com.wave.cofrinho.DTO.user.UserResponse;
import com.wave.cofrinho.exception.InputFieldException;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.repository.UserRepository;
import com.wave.cofrinho.service.AuthenticationService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {

    private final AuthenticationService authenticationService;
    private final CommonMapper commonMapper;
    private UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Map<String, Object> credentials = authenticationService.login(request.getEmail(), request.getPassword());
        AuthenticationResponse response = new AuthenticationResponse();
        response.setUser(commonMapper.convertToResponse(credentials.get("user"), UserResponse.class));
        response.setToken((String) credentials.get("token"));
        return response;
    }

    public String getEmailByPasswordResetCode(String code) {
        return authenticationService.getEmailByPasswordResetCode(code);
    }

    public AuthenticationResponse registerUser(String captcha, RegistrationRequest registrationRequest, BindingResult bindingResult, HttpServletResponse response) throws MessagingException {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        User user = commonMapper.convertToEntity(registrationRequest, User.class);
        return authenticationService.registerUser(user, captcha, registrationRequest.getPassword2(), response);
    }

    public AuthenticationResponse activateUser(String code, HttpServletResponse response) {
        return authenticationService.activateUser(code, response);
    }

    public String sendPasswordResetCode(String email) throws MessagingException {
        return authenticationService.sendPasswordResetCode(email);
    }

    public String passwordReset(String email, PasswordResetRequest passwordReset) {
        return authenticationService.passwordReset(email, passwordReset.getPassword(), passwordReset.getPassword2());
    }

    public String passwordReset(String email, PasswordResetRequest passwordReset, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return authenticationService.passwordReset(email, passwordReset.getPassword(), passwordReset.getPassword2());
        }
    }

    public String updateUserEmail(Long userId, String newEmail) throws MessagingException {
        User user = userRepository.findByEmail(newEmail);
        if (user != null) {
            throw new IllegalArgumentException("E-mail já está em uso");
        }
    
        user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return authenticationService.updateUserEmail(user, newEmail);
    }
    
}
