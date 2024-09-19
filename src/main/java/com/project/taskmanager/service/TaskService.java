package com.project.taskmanager.service;

import java.util.List;

import com.project.taskmanager.entity.Task;

public interface TaskService {

    List<Task> getAllTasks(String userId);

    Task createTask(String userId, Task task);

    Task getTaskById(String userId, String id);

    Task updateTask(String userId, String id, Task task);

    void deleteTask(String userId, String id);

}
