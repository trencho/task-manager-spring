package com.project.taskmanager.controller;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanager.dto.TaskDTO;
import com.project.taskmanager.entity.Task;
import com.project.taskmanager.mapper.TaskMapper;
import com.project.taskmanager.service.TaskService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(@AuthenticationPrincipal(expression = "username") final String username,
            final Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(username, pageable));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal(expression = "username") final String username,
            @Valid @RequestBody final TaskDTO taskDTO) throws URISyntaxException {
        final var task = taskMapper.toEntity(taskDTO);
        task.setUsername(username);
        final var createdTask = taskService.createTask(task);
        final var location = new URI("/api/tasks/" + createdTask.getId());

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@AuthenticationPrincipal(expression = "username") final String username,
            @PathVariable final String taskId) {
        final var task = taskService.getTaskById(username, taskId);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@AuthenticationPrincipal(expression = "username") final String username,
            @PathVariable final String taskId,
            @RequestBody final TaskDTO taskDTO) {
        final var task = taskService.updateTask(username, taskId, taskMapper.toEntity(taskDTO));
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal(expression = "username") final String username,
            @PathVariable final String taskId) {
        taskService.deleteTask(username, taskId);
        return ResponseEntity.noContent().build();
    }

}
