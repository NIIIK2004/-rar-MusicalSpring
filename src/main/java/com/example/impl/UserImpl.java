package com.example.impl;

import com.example.dao.UserDao;

import com.example.model.Role;
import com.example.model.User;
import com.example.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserDao {
    @Value("${upload.path}")
    private String uploadPath;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User saveAdmin(User user) {
        user.setRoles(Collections.singleton(Role.ADMIN));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepo.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User updateFields(Long id, String name, String surname, String mail, String username, String password, String avatar) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(name);
            existingUser.setSurname(surname);
            existingUser.setMail(mail);
            existingUser.setUsername(username);
            existingUser.setPassword(password);
            existingUser.setAvatar(avatar);
            return userRepo.save(existingUser);
        }
        throw new IllegalArgumentException("Invalid user Id: " + id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}