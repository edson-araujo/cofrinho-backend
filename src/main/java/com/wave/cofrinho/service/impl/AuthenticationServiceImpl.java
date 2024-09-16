package com.wave.cofrinho.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.wave.cofrinho.DTO.CaptchaResponse;
import com.wave.cofrinho.DTO.auth.AuthenticationResponse;
import com.wave.cofrinho.DTO.user.UserResponse;
import com.wave.cofrinho.Enum.AuthProvider;
import com.wave.cofrinho.Enum.Role;
import com.wave.cofrinho.Enum.TipoContaEnum;
import com.wave.cofrinho.configuration.EncryptionUtil;
import com.wave.cofrinho.exception.ApiRequestException;
import com.wave.cofrinho.exception.EmailException;
import com.wave.cofrinho.exception.PasswordConfirmationException;
import com.wave.cofrinho.exception.PasswordException;
import com.wave.cofrinho.model.Conta;
import com.wave.cofrinho.model.ContaCorrente;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.repository.UserRepository;
import com.wave.cofrinho.security.JwtProvider;
import com.wave.cofrinho.security.oauth2.OAuth2UserInfo;
import com.wave.cofrinho.service.AuthenticationService;
import com.wave.cofrinho.service.email.MailSender;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.wave.cofrinho.constants.ErrorMessage.ACTIVATION_CODE_NOT_FOUND;
import static com.wave.cofrinho.constants.ErrorMessage.EMAIL_IN_USE;
import static com.wave.cofrinho.constants.ErrorMessage.EMAIL_NOT_FOUND;
import static com.wave.cofrinho.constants.ErrorMessage.EMPTY_PASSWORD_CONFIRMATION;
import static com.wave.cofrinho.constants.ErrorMessage.INCORRECT_PASSWORD;
import static com.wave.cofrinho.constants.ErrorMessage.INVALID_PASSWORD_CODE;
import static com.wave.cofrinho.constants.ErrorMessage.PASSWORDS_DO_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${hostname}")
    private String hostname;

    @Value("${recaptcha.secret}")
    private String secret;

    @Value("${recaptcha.url}")
    private String captchaUrl;

    @Override
    public Map<String, Object> login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            String userRole = user.getRoles().iterator().next().name();
            String token = jwtProvider.createToken(email, userRole);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);
            return response;
        } catch (AuthenticationException e) {
            throw new ApiRequestException(INCORRECT_PASSWORD, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public AuthenticationResponse registerUser(User user, String captcha, String password2, HttpServletResponse response) throws MessagingException {
        String url = String.format(captchaUrl, secret, captcha);
        restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);

        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            throw new PasswordException(PASSWORDS_DO_NOT_MATCH);
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailException(EMAIL_IN_USE);
        }
        user.setActive(false);
        user.setCreatedAt(LocalDate.now());
        user.setRoles(Set.of(Role.USER)); 
        user.setProvider(AuthProvider.LOCAL);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNome("Conta Corrente Inicial");
        contaCorrente.setTipo(TipoContaEnum.CONTA_CORRENTE);
        contaCorrente.setCreatedAt(LocalDate.now());
        contaCorrente.setUsuario(user);
        contaCorrente.setLimite(new BigDecimal("00"));
        
        user.getContas().add(contaCorrente);
        userRepository.save(user);
        
        sendEmail(user, "Activation code", "registration-template", "registrationUrl",
               "/activate/" + user.getActivationCode());
        
        Role role = user.getRoles().stream().findFirst().orElse(Role.USER);
        String token = jwtProvider.createAndEncryptToken(user.getEmail(), role.toString());
        response.addCookie(createAuthCookie(token));
        return convertToDTO(userRepository.findByEmail(user.getEmail()));
    }

    public AuthenticationResponse convertToDTO(User user) {
        AuthenticationResponse dto = new AuthenticationResponse();
        UserResponse userDto = new UserResponse();
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setId(user.getId());
        userDto.setNickname(user.getNickname()); 

        dto.setActive(user.isActive());
        dto.setUser(userDto);
        dto.setToken("true");
        return dto;
    }

    private Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

    @Override
    @Transactional
    public User registerOauth2User(String provider, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setFirstname(oAuth2UserInfo.getFirstName());
        user.setLastname(oAuth2UserInfo.getLastName());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateOauth2User(User user, String provider, OAuth2UserInfo oAuth2UserInfo) {
        user.setFirstname(oAuth2UserInfo.getFirstName());
        user.setLastname(oAuth2UserInfo.getLastName());
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        return userRepository.save(user);
    }

    @Override
    public String getEmailByPasswordResetCode(String code) {
        return userRepository.getEmailByPasswordResetCode(code)
                .orElseThrow(() -> new ApiRequestException(INVALID_PASSWORD_CODE, HttpStatus.BAD_REQUEST));
    }

    @Override
    @Transactional
    public String sendPasswordResetCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        user.setPasswordResetCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendEmail(user, "Password reset", "password-reset-template", "resetUrl",
                "/reset/" + user.getPasswordResetCode());
        return "Reset password code is send to your E-mail";
    }

    @Override
    @Transactional
    public String passwordReset(String email, String password, String password2) {
        if (StringUtils.isEmpty(password2)) {
            throw new PasswordConfirmationException(EMPTY_PASSWORD_CONFIRMATION);
        }
        if (password != null && !password.equals(password2)) {
            throw new PasswordException(PASSWORDS_DO_NOT_MATCH);
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordResetCode(null);
        userRepository.save(user);
        return "Password successfully changed!";
    }

    @Override
    @Transactional
    public AuthenticationResponse activateUser(String code, HttpServletResponse response) {
        if (code == null || code.isEmpty()) {
            throw new ApiRequestException("Código de ativação inválido", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByActivationCode(code);
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

         Role role = user.getRoles().stream().findFirst().orElse(Role.USER);
        String token = jwtProvider.createAndEncryptToken(user.getEmail(), role.toString());
        response.addCookie(createAuthCookie(token));

        return convertToDTO(userRepository.findByEmail(user.getEmail()));
    }


    private void sendEmail(User user, String subject, String template, String urlAttribute, String urlPath)
            throws MessagingException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstname());
        attributes.put(urlAttribute, "http://" + hostname + urlPath);
        mailSender.sendMessageHtml(user.getEmail(), subject, template, attributes);
    }

    public String updateUserEmail(User user, String newEmail) throws MessagingException {
        user.setEmail(newEmail);
        userRepository.save(user);

        sendEmail(user, "Password reset", "password-reset-template", "resetUrl",
                "/reset/" + user.getPasswordResetCode());

        return "E-mail alterado com sucesso! Um novo código de ativação foi enviado para o seu novo e-mail.";
    }
}
