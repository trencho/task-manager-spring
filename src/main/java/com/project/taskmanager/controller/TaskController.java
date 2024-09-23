package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskDTO;
import com.project.taskmanager.entity.Task;
import com.project.taskmanager.mapper.TaskMapper;
import com.project.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(final Authentication authentication,
                                                  final Pageable pageable) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        return ResponseEntity.ok(taskService.getAllTasks(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(final Authentication authentication,
                                           @Valid @RequestBody final TaskDTO taskDTO) throws URISyntaxException {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        final var createdTask = taskService.createTask(userId, taskMapper.toEntity(taskDTO));
        final var location = new URI("/api/tasks/" + createdTask.getId());

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(final Authentication authentication, @PathVariable final String taskId) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        final var task = taskService.getTaskById(userId, taskId);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(final Authentication authentication, @PathVariable final String taskId,
                                              @RequestBody final TaskDTO taskDTO) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        final var task = taskService.updateTask(userId, taskId, taskMapper.toEntity(taskDTO));
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(final Authentication authentication, @PathVariable final String taskId) {
        final var principal = (User) authentication.getPrincipal();
        final var userId = principal.getUsername();

        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

}
