package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    @Value("/api/labels")
    private String url;

    @Autowired
    private LabelMapper labelMapper;

    @BeforeEach
    public void setUp() throws Exception {
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
    }
    @AfterEach
    public void clear() {
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get(url).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreateLabel() throws Exception {
        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testLabel);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Label label = labelRepository.findByName(testLabel.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with name: " + testLabel.getName()));

        assertThat(label).isNotNull();
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public  void testCreateLabelWithNotValidName() throws Exception {
        testLabel.setName("");
        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testLabel);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShowLabel() throws Exception {
        labelRepository.save(testLabel);

        MockHttpServletRequestBuilder request = get(url + "/{id}", testLabel.getId()).with(jwt());

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testUpdateLabel() throws Exception {
        labelRepository.save(testLabel);

        testLabel.setName("New name");

        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testLabel);

        var request = put(url + "/{id}", testLabel.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Label label = labelRepository.findByName(testLabel.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with name: " + testLabel.getName()));

        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testUpdateLabelWithNotValidName() throws Exception {
        labelRepository.save(testLabel);

        testLabel.setName("1");

        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testLabel);

        var request = put(url + "/{id}", testLabel.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        labelRepository.save(testLabel);

        var request = delete(url + "/{id}", testLabel.getId()).with(jwt());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(labelRepository.findByName(testLabel.getName())).isNotPresent();
    }
}
