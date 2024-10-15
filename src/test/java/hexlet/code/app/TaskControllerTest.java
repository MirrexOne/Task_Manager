package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.requests.LoginRequest;
import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private static final String BASE_URL = "/api/tasks";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer %s";

    private String token;
    private Task testTask;
    private User testUser;
    private TaskStatus testStatus;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser = userRepository.save(testUser);

        testStatus = new TaskStatus();
        testStatus.setName("In Progress");
        testStatus.setSlug("in_progress");
        testStatus = taskStatusRepository.save(testStatus);

        testTask = new Task();
        testTask.setName("Test Task");
        testTask.setDescription("Test Description");
        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        testTask = taskRepository.save(testTask);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password");
        token = authService.login(loginRequest);
    }

    @Test
    void testCreateTask() throws Exception {
        TaskDto.Request request = new TaskDto.Request();
        request.setTitle("New Task");
        request.setContent("New Description");
        request.setStatus("in_progress");
        request.setAssigneeId(testUser.getId());

        mockMvc.perform(post(BASE_URL)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.content").value("New Description"))
                .andExpect(jsonPath("$.status").value("in_progress"))
                .andExpect(jsonPath("$.assignee_id").value(testUser.getId()));
    }

    @Test
    void testGetTask() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", testTask.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTask.getId()))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.content").value("Test Description"))
                .andExpect(jsonPath("$.status").value("in_progress"))
                .andExpect(jsonPath("$.assignee_id").value(testUser.getId()));
    }

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testTask.getId()))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].content").value("Test Description"))
                .andExpect(jsonPath("$[0].status").value("in_progress"))
                .andExpect(jsonPath("$[0].assignee_id").value("8"));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskDto.Request request = new TaskDto.Request();
        request.setTitle("Updated Task");
        request.setContent("Updated Description");
        request.setStatus("in_progress");
        request.setAssigneeId(testUser.getId());

        mockMvc.perform(put(BASE_URL + "/{id}", testTask.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTask.getId()))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.content").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("in_progress"))
                .andExpect(jsonPath("$.assignee_id").value(testUser.getId()));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", testTask.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", testTask.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskDto.Request())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put(BASE_URL + "/{id}", testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskDto.Request())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(BASE_URL + "/{id}", testTask.getId()))
                .andExpect(status().isUnauthorized());
    }
}
