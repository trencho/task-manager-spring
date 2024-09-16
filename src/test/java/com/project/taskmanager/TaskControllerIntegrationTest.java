package com.project.taskmanager;

import com.project.taskmanager.config.MongoTestContainerConfig;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = MongoTestContainerConfig.class)
@SpringBootTest
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoDBContainer mongoDBContainer;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        final var mongoUri = mongoDBContainer.getReplicaSetUrl();
        System.setProperty("spring.data.mongodb.uri", mongoUri);

        taskRepository.deleteAll();
    }

    @Test
    void testCreateTask() throws Exception {
        final var newTaskJson = "{\"title\":\"Test Task\",\"description\":\"Test Description\"}";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        final var task1 = new Task("Task 1", "Description 1", false, "testUser");
        final var task2 = new Task("Task 2", "Description 2", false, "testUser");
        taskRepository.saveAll(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testDeleteTask() throws Exception {
        final var task = new Task("Test Task", "Test Description", false, "testUser");
        taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk());
    }

}
