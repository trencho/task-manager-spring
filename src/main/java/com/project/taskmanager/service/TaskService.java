package com.project.taskmanager.service;

import com.project.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<Task> getAllTasks(String userId, Pageable pageable);

    Task createTask(String userId, Task task);

    Task getTaskById(String userId, String id);

    Task updateTask(String userId, String id, Task task);

    void deleteTask(String userId, String id);

}
