package com.project.spring.service;

import com.project.spring.model.User;
import com.project.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null); // Clear the reset token after password reset
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
