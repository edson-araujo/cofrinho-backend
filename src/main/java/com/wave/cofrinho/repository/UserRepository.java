package com.wave.cofrinho.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wave.cofrinho.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    List<User> findAllByOrderByIdAsc();

    @Query("SELECT u FROM User u WHERE u.activationCode = :code")
    User findByActivationCode(@Param("code") String code);

    User findByEmail(String email);

    @Query("SELECT user.email FROM User user WHERE user.passwordResetCode = :code")
    Optional<String> getEmailByPasswordResetCode(String code);
}
