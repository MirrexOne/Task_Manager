package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initializeUser();
        initializeTaskStatuses();
        initializeLabels();
    }

    private void initializeUser() {
        if (userRepository.findByEmail("mirrex@dev.io").isEmpty()) {
            var user = new User();
            user.setEmail("mirrex@dev.io");
            user.setPasswordDigest(passwordEncoder.encode("qwerty"));
            userRepository.save(user);
        }
    }

    private void initializeTaskStatuses() {
        if (taskStatusRepository.count() == 0) {
            Map<String, String> statuses = new HashMap<>();
            statuses.put("Draft", "draft");
            statuses.put("ToReview", "to_review");
            statuses.put("ToBeFixed", "to_be_fixed");
            statuses.put("ToPublish", "to_publish");
            statuses.put("Published", "published");

            statuses.forEach((name, slug) -> {
                TaskStatus status = new TaskStatus();
                status.setName(name);
                status.setSlug(slug);
                taskStatusRepository.save(status);
            });
        }
    }

    private void initializeLabels() {
        if (labelRepository.count() == 0) {
            labelRepository.save(new Label("feature"));
            labelRepository.save(new Label("bug"));
        }
    }
}
