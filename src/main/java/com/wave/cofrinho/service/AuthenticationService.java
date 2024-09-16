package com.wave.cofrinho.service;

import java.util.Map;

import com.wave.cofrinho.DTO.auth.AuthenticationResponse;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.security.oauth2.OAuth2UserInfo;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    Map<String, Object> login(String email, String password);

    AuthenticationResponse registerUser(User user, String captcha, String password2, HttpServletResponse response) throws MessagingException;

    User registerOauth2User(String provider, OAuth2UserInfo oAuth2UserInfo);

    User updateOauth2User(User user, String provider, OAuth2UserInfo oAuth2UserInfo);

    AuthenticationResponse activateUser(String code, HttpServletResponse response);

    String getEmailByPasswordResetCode(String code);

    String sendPasswordResetCode(String email) throws MessagingException;

    String passwordReset(String email, String password, String password2);

    String updateUserEmail(User user, String newEmail) throws MessagingException;
}
