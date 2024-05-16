package com.example.repo;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByMail(String email);
    long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

}