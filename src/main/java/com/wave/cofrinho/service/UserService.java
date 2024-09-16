package com.wave.cofrinho.service;

import com.wave.cofrinho.model.User;

public interface UserService {

    User getUserById(Long userId);

    User getUserInfo(String email);
    
    User updateUserInfo(String email, User user);
}