package com.project.taskmanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.taskmanager.entity.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByUserId(String userId);

}
