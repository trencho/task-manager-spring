package com.project.taskmanager;

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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.impl.TaskServiceImpl;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId("userId");
        user.setUsername("username");
    }

    @Test
    void testGetAllTasks() {
        final var userId = "username";
        when(taskRepository.findByUserId(userId)).thenReturn(List.of());

        final var tasks = taskService.getAllTasks(userId);
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testCreateTask() {
        final var task = new Task("Test Task", "Test Description", false, user);

        when(taskRepository.save(task)).thenReturn(task);

        final var createdTask = taskService.createTask("username", task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTaskByIdSuccessful() {
        final var task = new Task("Test Task", "Test Description", false, user);
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        final var foundTask = taskService.getTaskById("username", "1");
        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());

        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testGetTaskByIdFailed() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById( "username", "1"));

        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateTaskSuccessful() {
        final var taskId = "1";
        final var existingTask = new Task("Old Title", "Old Description", false, user);
        final var updatedTask = new Task("New Title", "New Description", true, user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        final var result = taskService.updateTask("username", taskId, updatedTask);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isCompleted());

        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskFailed() {
        final var taskId = "1";
        final var updatedTask = new Task("New Title", "New Description", true, user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask("username", taskId, updatedTask));

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testDeleteTask() {
        final var task = new Task("Test Task", "Test Description", false, user);
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById("1");

        taskService.deleteTask("username", "1");

        verify(taskRepository, times(1)).deleteById("1");
    }

}
