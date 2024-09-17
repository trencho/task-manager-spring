package com.project.taskmanager.service.impl;

import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(final Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(final String id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    @Override
    public Task updateTask(final String id, final Task task) {
        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Task taskToUpdate = existingTask.get();
            taskToUpdate.setTitle(task.getTitle());
            taskToUpdate.setDescription(task.getDescription());
            taskToUpdate.setCompleted(task.isCompleted());

            return taskRepository.save(taskToUpdate);
        }

        throw new TaskNotFoundException("Task not found with id: " + id);
    }

    @Override
    public void deleteTask(final String id) {
        taskRepository.deleteById(id);
    }

}
