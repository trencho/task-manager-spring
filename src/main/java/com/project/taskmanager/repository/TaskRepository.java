package com.project.taskmanager.repository;

import com.project.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {

    Page<Task> findByUsername(String username, Pageable pageable);

}
