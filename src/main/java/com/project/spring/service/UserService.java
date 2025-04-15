package com.project.spring.service;

import com.project.spring.model.User;

import java.util.Optional;

public interface UserService {
    void registerUser(User user);
    Optional<User> findByUsername(String username);
}