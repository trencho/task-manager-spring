package com.project.taskmanager.service;

import com.project.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<Task> getAllTasks(String username, Pageable pageable);

    Task createTask(Task task);

    Task getTaskById(String username, String id);

    Task updateTask(String username, String id, Task task);

    void deleteTask(String username, String id);

}
