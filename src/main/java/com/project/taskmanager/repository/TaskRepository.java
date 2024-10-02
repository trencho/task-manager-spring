package com.project.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.taskmanager.entity.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

    Page<Task> findByUsername(String username, Pageable pageable);

}
