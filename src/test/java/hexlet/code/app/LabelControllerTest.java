package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDto;
import hexlet.code.entities.Label;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.dto.requests.LoginRequest;
import hexlet.code.dto.requests.CreateUserRequest;
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
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String BASE_URL = "/api/labels";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer %s";

    private String token;
    private Label testLabel;

    @BeforeEach
    void setUp() throws Exception {
        labelRepository.deleteAll();
        userRepository.deleteAll();

        testLabel = new Label();
        testLabel.setName("Test Label");
        testLabel = labelRepository.save(testLabel);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setFirstName("Test");
        createUserRequest.setLastName("User");
        createUserRequest.setPassword("password");
        authService.createUser(createUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password");
        token = authService.login(loginRequest);
    }

    @Test
    void testCreateLabel() throws Exception {
        LabelDto.Request request = new LabelDto.Request();
        request.setName("New Label");

        mockMvc.perform(post(BASE_URL)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Label"));
    }

    @Test
    void testGetLabel() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", testLabel.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testLabel.getId()))
                .andExpect(jsonPath("$.name").value("Test Label"));
    }

    @Test
    void testGetAllLabels() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testLabel.getId()))
                .andExpect(jsonPath("$[0].name").value("Test Label"));
    }

    @Test
    void testUpdateLabel() throws Exception {
        LabelDto.Request request = new LabelDto.Request();
        request.setName("Updated Label");

        mockMvc.perform(put(BASE_URL + "/{id}", testLabel.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testLabel.getId()))
                .andExpect(jsonPath("$.name").value("Updated Label"));
    }

    @Test
    void testDeleteLabel() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", testLabel.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", testLabel.getId())
                        .header(AUTH_HEADER, String.format(BEARER_TOKEN, token)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LabelDto.Request())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put(BASE_URL + "/{id}", testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LabelDto.Request())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(BASE_URL + "/{id}", testLabel.getId()))
                .andExpect(status().isUnauthorized());
    }
}
