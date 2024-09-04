package com.project.taskmanager.service;

import java.util.List;

import com.project.taskmanager.model.Task;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTaskById(String id);

    Task createTask(Task task);

    Task updateTask(String id, Task task);

    boolean deleteTask(String id);

}
