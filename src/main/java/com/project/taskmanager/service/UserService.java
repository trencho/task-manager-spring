package com.project.taskmanager.service;

import java.util.Optional;

import com.project.taskmanager.model.User;

public interface UserService {

    User registerUser(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findById(String userId);

}
