package com.wave.cofrinho.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.model.User;
import com.wave.cofrinho.repository.UserRepository;
import com.wave.cofrinho.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.wave.cofrinho.exception.ApiRequestException;
import static com.wave.cofrinho.constants.ErrorMessage.EMAIL_NOT_FOUND;
import static com.wave.cofrinho.constants.ErrorMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public User getUserInfo(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUserInfo(String email, User user) {
        User userFromDb = userRepository.findByEmail(email);
        if (userFromDb == null) {
            throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        userFromDb.setFirstname(user.getFirstname());
        userFromDb.setLastname(user.getLastname());
        return userFromDb;
    }
    

}
