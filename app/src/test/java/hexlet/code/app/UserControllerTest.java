package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testGetUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);

        mockMvc.perform(get("/api/users/" + userResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testUpdateUser() throws Exception {
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setEmail("test@example.com");
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setPassword("password");

        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setFirstName("Jane");

        mockMvc.perform(put("/api/users/" + userResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);

        mockMvc.perform(delete("/api/users/" + userResponse.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + userResponse.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserWithInvalidData() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("invalid-email");
        request.setFirstName("J");
        request.setLastName("D");
        request.setPassword("");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}