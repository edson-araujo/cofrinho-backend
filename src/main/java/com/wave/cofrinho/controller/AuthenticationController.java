package com.wave.cofrinho.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wave.cofrinho.DTO.PasswordResetRequest;
import com.wave.cofrinho.DTO.auth.AuthenticationRequest;
import com.wave.cofrinho.DTO.auth.AuthenticationResponse;
import com.wave.cofrinho.configuration.EncryptionUtil;
import com.wave.cofrinho.mapper.AuthenticationMapper;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.repository.UserRepository;
import com.wave.cofrinho.security.JwtProvider;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import static com.wave.cofrinho.constants.PathConstants.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_AUTH)
public class AuthenticationController {

    private final AuthenticationMapper authenticationMapper;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @PostMapping(LOGIN)
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request,
            HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationMapper.login(request);

        Cookie jwtCookie = new Cookie("JWT_TOKEN", authResponse.getToken());
        jwtCookie.setHttpOnly(true); // Protege contra XSS
        jwtCookie.setSecure(false); // Define o cookie para ser usado apenas em HTTPS
        jwtCookie.setPath("/"); // Define o caminho do cookie
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Define a duração do cookie (por exemplo, 7 dias)

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping(FORGOT_EMAIL)
    public ResponseEntity<String> forgotPassword(@PathVariable String email) throws MessagingException {
        return ResponseEntity.ok(authenticationMapper.sendPasswordResetCode(email));
    }

    @GetMapping(RESET_CODE)
    public ResponseEntity<String> getEmailByPasswordResetCode(@PathVariable String code) {
        return ResponseEntity.ok(authenticationMapper.getEmailByPasswordResetCode(code));
    }

    @PostMapping(RESET)
    public ResponseEntity<String> passwordReset(@RequestBody PasswordResetRequest passwordReset) {
        return ResponseEntity.ok(authenticationMapper.passwordReset(passwordReset.getEmail(), passwordReset));
    }

    @GetMapping(VALIDATE)
    public ResponseEntity<Boolean> isTokenValido(@PathVariable String token) {
        return ResponseEntity.ok(jwtProvider.validateToken(token));
    }

    @GetMapping(STATUS_CONTA)
    public ResponseEntity<?> getAccountStatus(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        token = EncryptionUtil.decrypt(token);
        Map<String, Object> response = new HashMap<>();
        response.put("token", false);

        if (token != null && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            if (authentication != null) {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    response.put("token", true);
                    response.put("active", user.isActive());
                    response.put("userId", user.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
                }
            }
        }

        return ResponseEntity.ok(response);
    }
}