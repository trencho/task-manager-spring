package com.project.taskmanager.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanager.model.Task;
import com.project.taskmanager.service.TaskService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(final Authentication authentication) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        return ResponseEntity.ok(taskService.getAllTasks(userId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(final Authentication authentication,
            @Valid @RequestBody final Task task) throws URISyntaxException {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        final var createdTask = taskService.createTask(userId, task);
        final var location = new URI("/api/tasks/" + createdTask.getId());

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(final Authentication authentication, @PathVariable final String taskId) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        return ResponseEntity.ok(taskService.getTaskById(userId, taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(final Authentication authentication, @PathVariable final String taskId,
            @RequestBody final Task task) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        return ResponseEntity.ok(taskService.updateTask(userId, taskId, task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(final Authentication authentication, @PathVariable final String taskId) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

}
