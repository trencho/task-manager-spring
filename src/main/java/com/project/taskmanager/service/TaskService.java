package com.project.taskmanager.service;

import com.project.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    Task createTask(Task task);

    Task getTaskById(String id);

    Task updateTask(String id, Task task);

    void deleteTask(String id);

}
