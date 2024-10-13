package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.LoginRequest;
import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private static final String BASE_URL = "/api/users";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer %s";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_PASSWORD = "password";

    private String token;
    private Long userId;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        UserResponse user = createTestUser(DEFAULT_EMAIL, "John", "Doe", DEFAULT_PASSWORD);
        userId = user.getId();
        token = getAuthToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("newuser@example.com");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setPassword(DEFAULT_PASSWORD);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + userId)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setFirstName("Jane");

        mockMvc.perform(put(BASE_URL + "/" + userId)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    void testDeleteUser() throws Exception {
        UserResponse user = createTestUser("deletetest@example.com", "Delete", "Test", DEFAULT_PASSWORD);
        String deleteToken = getAuthToken("deletetest@example.com", DEFAULT_PASSWORD);

        mockMvc.perform(delete(BASE_URL + "/" + user.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, deleteToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/" + user.getId()))
                .andExpect(status().isUnauthorized());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    void testCreateUserWithInvalidData() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("invalid-email");
        request.setFirstName("J");
        request.setLastName("D");
        request.setPassword("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(DEFAULT_EMAIL));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + userId))
                .andExpect(status().isUnauthorized());
    }

    private UserResponse createTestUser(String email, String firstName, String lastName, String password) {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPassword(password);
        return authService.createUser(request);
    }

    private String getAuthToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return authService.login(loginRequest);
    }
}
