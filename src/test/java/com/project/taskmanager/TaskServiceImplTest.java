package com.project.taskmanager;

import com.project.taskmanager.entity.Task;
import com.project.taskmanager.enums.TaskStatus;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    private static final String USERNAME = "username";

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetAllTasks() {
        when(taskRepository.findByUsername(USERNAME)).thenReturn(List.of());

        final var tasks = taskService.getAllTasks(USERNAME);
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testCreateTask() {
        final var task = new Task("Test Task", "Test Description", LocalDate.now(), TaskStatus.PENDING, USERNAME);

        when(taskRepository.save(task)).thenReturn(task);

        final var createdTask = taskService.createTask(USERNAME, task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTaskByIdSuccessful() {
        final var task = new Task("Test Task", "Test Description", LocalDate.now(), TaskStatus.PENDING, USERNAME);
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        final var foundTask = taskService.getTaskById(USERNAME, "1");
        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());

        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testGetTaskByIdFailed() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(USERNAME, "1"));

        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateTaskSuccessful() {
        final var taskId = "1";
        final var existingTask = new Task("Old Title", "Old Description", LocalDate.now(), TaskStatus.PENDING, USERNAME);
        final var updatedTask = new Task("New Title", "New Description", LocalDate.now(), TaskStatus.COMPLETED, USERNAME);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        final var result = taskService.updateTask(USERNAME, taskId, updatedTask);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertSame(TaskStatus.COMPLETED, result.getStatus());

        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskFailed() {
        final var taskId = "1";
        final var updatedTask = new Task("New Title", "New Description", LocalDate.now(), TaskStatus.COMPLETED, USERNAME);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(USERNAME, taskId, updatedTask));

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testDeleteTask() {
        final var task = new Task("Test Task", "Test Description", LocalDate.now(), TaskStatus.PENDING, USERNAME);
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById("1");

        taskService.deleteTask(USERNAME, "1");

        verify(taskRepository, times(1)).deleteById("1");
    }

}
