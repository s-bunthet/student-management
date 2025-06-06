package com.project.spring.repository;

import com.project.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email) ;
    Optional<User> findByUsername(String username);
    Optional<User> findByResetToken(String resetToken);
}