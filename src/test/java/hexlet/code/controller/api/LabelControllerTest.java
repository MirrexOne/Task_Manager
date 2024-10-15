package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    private User testUser;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }


    @Test
    public void testShow() throws Exception {
        var request = get("/api/labels/{id}", testLabel.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new LabelCreateDTO();
        dto.setName("Spring");

        mockMvc.perform(post("/api/labels")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var label = labelRepository.findByNameWithEagerUpload(dto.getName()).orElseThrow();

        assertThat(label.getName()).isEqualTo(dto.getName());
        assertThat(label.getName()).isNotNull();
    }

    @Test
    public void testCreateWithNotValidTitle() throws Exception {
        var dto = labelMapper.map(testLabel);
        dto.setName("");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        var dto = labelMapper.map(testLabel);
        dto.setName("Java");

        var request = put("/api/labels/{id}", testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelRepository.findByNameWithEagerUpload(dto.getName()).orElseThrow();

        assertThat(label.getName()).isEqualTo("Java");
        assertThat(label.getTasks().size()).isEqualTo(testLabel.getTasks().size());
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/labels/{id}", testLabel.getId())
                .with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(labelRepository.existsById(testLabel.getId())).isEqualTo(false);
    }

    @Test
    public void bidirectionalNoSync() {
        //PERSIST
        Task task = new Task();
        task.setName("Metro");
        task.setDescription("Build metro station");
        taskRepository.save(task);

        Label label = new Label();
        label.setName("label");

        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        label.setTasks(tasks);

        labelRepository.save(label);

        //MERGE
        label.setName("Metropolitan");
        task.setDescription("Destroy metro station");

        labelRepository.save(label);

        Label saved = labelRepository.findByNameWithEagerUpload("Metropolitan").orElseThrow();
        assertThat(saved.getTasks()).isEmpty();
    }

    @Test
    public void bidirectionalIncorrectSync() {
        //PERSIST
        Task task = new Task();
        task.setName("Metro");
        task.setDescription("Build metro station");
        taskRepository.save(task);

        Label label = new Label();
        label.setName("label");
        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        label.setTasks(tasks);

        Set<Label> labels = new HashSet<>();
        labels.add(label);
        task.setLabels(labels);

        labelRepository.save(label);

        //MERGE
        label.setName("Metropolitan");
        task.setDescription("Destroy metro station");

        labelRepository.save(label);

        Label saved = labelRepository.findByNameWithEagerUpload("Metropolitan").orElseThrow();
        assertThat(saved.getTasks()).isNotEmpty();
    }

    @Test
    public void bidirectionalCorrectSync() {
        //PERSIST
        Task task = new Task();
        task.setName("Metro");
        task.setDescription("Build metro station");
        taskRepository.save(task);

        Label label = new Label();
        label.setName("label");

        System.out.println("save label");
        labelRepository.save(label);

        //MERGE
        label.setName("Metropolitan");
        task.setDescription("Destroy metro station");

        label.addTask(task);

        System.out.println("update label");
        labelRepository.save(label);

        Label saved = labelRepository.findByNameWithEagerUpload("Metropolitan").orElseThrow();
        Task savedTask = taskRepository.findByIdWithEagerUpload(task.getId()).orElseThrow();
        assertThat(saved.getTasks()).isNotEmpty();
        assertThat(savedTask.getLabels()).isNotEmpty();
    }
}
