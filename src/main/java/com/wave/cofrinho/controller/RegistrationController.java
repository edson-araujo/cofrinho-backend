package com.wave.cofrinho.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.wave.cofrinho.DTO.RegistrationRequest;
import com.wave.cofrinho.DTO.auth.AuthenticationResponse;
import com.wave.cofrinho.mapper.AuthenticationMapper;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.security.UserPrincipal;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import static com.wave.cofrinho.constants.PathConstants.ACTIVATE_CODE;
import static com.wave.cofrinho.constants.PathConstants.API_V1_REGISTRATION;
import static com.wave.cofrinho.constants.PathConstants.UPDATE_EMAIL;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_REGISTRATION)
public class RegistrationController {

    private final AuthenticationMapper authenticationMapper;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registration(@Valid @RequestBody RegistrationRequest user,
            BindingResult bindingResult, HttpServletResponse response) throws MessagingException {
        return ResponseEntity.ok(authenticationMapper.registerUser(user.getCaptcha(), user, bindingResult, response));
    }

    @GetMapping(ACTIVATE_CODE)
    public ResponseEntity<AuthenticationResponse> activateEmailCode(@PathVariable String code, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationMapper.activateUser(code, response));
    }

    @PutMapping(UPDATE_EMAIL)
    public ResponseEntity<String> updateUserEmail(@AuthenticationPrincipal UserPrincipal user,
            @RequestBody Map<String, String> requestBody) throws MessagingException {
        String newEmail = requestBody.get("newEmail"); // Obtenha o novo email do corpo da requisição
        return ResponseEntity.ok(authenticationMapper.updateUserEmail(user.getId(), newEmail));
    }
}
