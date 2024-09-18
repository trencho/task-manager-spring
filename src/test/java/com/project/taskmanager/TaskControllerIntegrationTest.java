package com.project.taskmanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.project.taskmanager.config.MongoTestContainerConfig;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.repository.UserRepository;

@AutoConfigureMockMvc
@ContextConfiguration(classes = MongoTestContainerConfig.class)
@SpringBootTest
class TaskControllerIntegrationTest {

    private static final String BASE_URL = "/api/tasks";
    private static final String TASK_JSON = "{\"title\": \"New Task Title\", \"description\": \"New Task Description\"}";
    private static final String UPDATED_TASK_JSON = "{\"title\": \"Updated Task Title\", \"description\": \"Updated Task Description\"}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        userRepository.save(user);

        task = new Task();
        task.setTitle("Initial Task Title");
        task.setDescription("Initial Task Description");
        task.setUser(user);
        taskRepository.save(task);
    }

    private String createTask(final String jsonPayload) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task Title"))
                .andExpect(jsonPath("$.description").value("New Task Description"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Disabled
    @Test
    @WithMockUser(username = "username")
    void testGetAllTasks() throws Exception {
        final var task1 = new Task("Task 1", "Description 1", false, user);
        final var task2 = new Task("Task 2", "Description 2", true, user);
        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));
    }

    @Test
    @WithMockUser(username = "username")
    void testCreateTask() throws Exception {
        createTask(TASK_JSON);
    }

    @Test
    @WithMockUser(username = "username")
    void testGetTaskSuccessful() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    @WithMockUser(username = "username1")
    void testGetTaskFailed() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "username")
    void testUpdateTaskSuccessful() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATED_TASK_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task Title"))
                .andExpect(jsonPath("$.description").value("Updated Task Description"));
    }

    @Test
    @WithMockUser(username = "username1")
    void testUpdateTaskFailedMissingTask() throws Exception {
        mockMvc.perform(put(BASE_URL + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATED_TASK_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found with id: 100"));
    }

    @Test
    @WithMockUser(username = "username1")
    void testUpdateTaskFailedIncorrectUser() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATED_TASK_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found for user: username1"));
    }

    @Test
    @WithMockUser(username = "username")
    void testDeleteTaskSuccessful() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "username1")
    void testDeleteTaskFailedMissingTask() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found with id: 100"));
    }

    @Test
    @WithMockUser(username = "username1")
    void testDeleteTaskFailedIncorrectUser() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found for user: username1"));
    }

}
