package com.project.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.taskmanager.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

}
