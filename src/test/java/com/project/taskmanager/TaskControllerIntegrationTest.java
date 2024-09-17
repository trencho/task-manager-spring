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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = MongoTestContainerConfig.class)
@SpringBootTest
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private Task task;
    private static final String BASE_URL = "/api/tasks";
    private static final String TASK_JSON = "{\"title\": \"New Task Title\", \"description\": \"New Task Description\"}";
    private static final String UPDATED_TASK_JSON = "{\"title\": \"Updated Task Title\", \"description\": \"Updated Task Description\"}";

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Initial Task Title");
        task.setDescription("Initial Task Description");
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

    @Test
    @WithMockUser(username = "testUser")
    void testGetAllTasks() throws Exception {
        createTask(TASK_JSON);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task.getDescription()))
                .andExpect(jsonPath("$[1].title").value("New Task Title"))
                .andExpect(jsonPath("$[1].description").value("New Task Description"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testCreateTask() throws Exception {
        createTask(TASK_JSON);
    }

    @Test
    @WithMockUser(username = "testUser")
    void testGetTask() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testUpdateTask() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATED_TASK_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task Title"))
                .andExpect(jsonPath("$.description").value("Updated Task Description"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
