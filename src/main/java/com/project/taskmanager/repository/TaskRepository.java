package com.project.taskmanager.repository;

import com.project.taskmanager.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByUsername(String username);

}
