package com.example.dao;

import com.example.model.User;

import java.util.List;
import java.util.Optional;
public interface UserDao {
    public User save(User user);

    User saveAdmin(User user);

    public void delete(Long id);

    public List<User> findAll();

    public Optional<User> findById(Long id);
    public User findByUsername(String username);

    User updateFields(Long id, String name, String surname, String mail, String username, String password, String avatar);
}
