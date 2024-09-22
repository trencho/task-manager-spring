package com.project.taskmanager.service;

import com.project.taskmanager.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks(String userId);

    Task createTask(String userId, Task task);

    Task getTaskById(String userId, String id);

    Task updateTask(String userId, String id, Task task);

    void deleteTask(String userId, String id);

}
