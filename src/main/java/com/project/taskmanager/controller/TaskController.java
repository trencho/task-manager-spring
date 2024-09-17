package com.project.taskmanager.controller;

import com.project.taskmanager.model.Task;
import com.project.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody final Task task) throws URISyntaxException {
        final var createdTask = taskService.createTask(task);
        final var location = new URI("/api/tasks/" + createdTask.getId());

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable final String taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable final String taskId, @RequestBody final Task task) {
        return ResponseEntity.ok(taskService.updateTask(taskId, task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable final String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
