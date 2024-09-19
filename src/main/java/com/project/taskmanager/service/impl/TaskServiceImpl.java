package com.project.taskmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.taskmanager.entity.Task;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.TaskService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_FOUND_WITH_ID = "Task not found with id: ";
    private static final String TASK_NOT_FOUND_FOR_USER = "Task not found for user: ";

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks(final String userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task createTask(final String userId, final Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(final String userId, final String id) {
        final var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_WITH_ID + id));
        if (task.getUser().getUsername().equals(userId)) {
            return task;
        }

        throw new TaskNotFoundException(TASK_NOT_FOUND_FOR_USER + userId);
    }

    @Override
    public Task updateTask(final String userId, final String id, final Task task) {
        final var existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_WITH_ID + id));

        if (existingTask.getUser().getUsername().equals(userId)) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setStatus(task.getStatus());

            return taskRepository.save(existingTask);
        }

        throw new TaskNotFoundException(TASK_NOT_FOUND_FOR_USER + userId);
    }

    @Override
    public void deleteTask(final String userId, final String id) {
        final var existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_WITH_ID + id));
        if (existingTask.getUser().getUsername().equals(userId)) {
            taskRepository.deleteById(id);
            return;
        }

        throw new TaskNotFoundException(TASK_NOT_FOUND_FOR_USER + userId);
    }

}
