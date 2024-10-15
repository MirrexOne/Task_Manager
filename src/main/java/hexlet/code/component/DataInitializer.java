package hexlet.code.component;

import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initializeAdminUser();
        initializeDefaultTaskStatuses();
    }

    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("mirrex@example.com")) {
            User admin = new User();
            admin.setEmail("mirrex@example.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPassword(passwordEncoder.encode("qwerty"));
            userRepository.save(admin);
        }
    }

    private void initializeDefaultTaskStatuses() {
        List<String> defaultSlugs = Arrays.asList("draft", "to_review", "to_be_fixed", "to_publish", "published");
        for (String slug : defaultSlugs) {
            if (!taskStatusRepository.existsBySlug(slug)) {
                TaskStatus status = new TaskStatus();
                status.setSlug(slug);
                status.setName(slug.replace("_", " ").toUpperCase());
                taskStatusRepository.save(status);
            }
        }
    }
}
