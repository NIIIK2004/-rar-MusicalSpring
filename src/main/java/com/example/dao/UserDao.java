package com.example.dao;

import com.example.model.User;

import java.util.List;
import java.util.Optional;
public interface UserDao {
    User save(User user);

    void saveAdmin(User user);

    void delete(Long id);

    List<User> findAll();

    Optional<User> findById(Long id);
    User findByUsername(String username);

    void updateFields(Long id, String name, String surname, String mail, String username, String password, String avatar);

    User findByEmail(String email);
}
