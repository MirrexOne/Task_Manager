package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entities.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskStatus testStatus;

    @BeforeEach
    void setUp() {
        taskStatusRepository.deleteAll();
        testStatus = new TaskStatus();
        testStatus.setName("Test Status");
        testStatus.setSlug("test_status");
        testStatus = taskStatusRepository.save(testStatus);
    }

    @Test
    void testGetTaskStatus() throws Exception {
        mockMvc.perform(get("/api/task_statuses/{id}", testStatus.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStatus.getId()))
                .andExpect(jsonPath("$.name").value("Test Status"))
                .andExpect(jsonPath("$.slug").value("test_status"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testGetAllTaskStatuses() throws Exception {
        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testStatus.getId()))
                .andExpect(jsonPath("$[0].name").value("Test Status"))
                .andExpect(jsonPath("$[0].slug").value("test_status"))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    @WithMockUser
    void testCreateTaskStatus() throws Exception {
        TaskStatusDto.Request request = new TaskStatusDto.Request();
        request.setName("New Status");
        request.setSlug("new_status");

        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Status"))
                .andExpect(jsonPath("$.slug").value("new_status"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser
    void testUpdateTaskStatus() throws Exception {
        TaskStatusDto.Request request = new TaskStatusDto.Request();
        request.setName("Updated Status");
        request.setSlug("updated_status");

        mockMvc.perform(put("/api/task_statuses/{id}", testStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStatus.getId()))
                .andExpect(jsonPath("$.name").value("Updated Status"))
                .andExpect(jsonPath("$.slug").value("updated_status"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser
    void testDeleteTaskStatus() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/{id}", testStatus.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/task_statuses/{id}", testStatus.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTaskStatusUnauthorized() throws Exception {
        TaskStatusDto.Request request = new TaskStatusDto.Request();
        request.setName("New Status");
        request.setSlug("new_status");

        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateTaskStatusUnauthorized() throws Exception {
        TaskStatusDto.Request request = new TaskStatusDto.Request();
        request.setName("Updated Status");
        request.setSlug("updated_status");

        mockMvc.perform(put("/api/task_statuses/{id}", testStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteTaskStatusUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/{id}", testStatus.getId()))
                .andExpect(status().isUnauthorized());
    }
}
