package com.project.taskmanager;

import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of());

        final var tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreateTask() {
        final var task = new Task("Test Task", "Test Description", false, "testUser");

        when(taskRepository.save(task)).thenReturn(task);

        final var createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTaskById() {
        final var task = new Task("Test Task", "Test Description", false, "testUser");
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        final var foundTask = taskService.getTaskById("1");
        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());

        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateTaskSuccessfully() {
        final var taskId = "1";
        final var existingTask = new Task("Old Title", "Old Description", false, "testUser");
        final var updatedTask = new Task("New Title", "New Description", true, "testUser");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        final var result = taskService.updateTask(taskId, updatedTask);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isCompleted());

        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskFailed() {
        final var taskId = "1";
        final var updatedTask = new Task("New Title", "New Description", true, "testUser");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(taskId, updatedTask);
        });

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById("1")).thenReturn(true);
        doNothing().when(taskRepository).deleteById("1");

        taskService.deleteTask("1");

        verify(taskRepository, times(1)).deleteById("1");
    }

}
