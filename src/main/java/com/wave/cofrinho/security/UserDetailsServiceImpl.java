package com.wave.cofrinho.security;

import lombok.RequiredArgsConstructor;

import static com.wave.cofrinho.constants.ErrorMessage.EMAIL_NOT_FOUND;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wave.cofrinho.exception.ApiRequestException;
import com.wave.cofrinho.model.User;
import com.wave.cofrinho.repository.UserRepository;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiRequestException(EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return UserPrincipal.create(user);
    }
}
