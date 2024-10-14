package hexlet.code.component;

import hexlet.code.entities.Label;
import hexlet.code.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(String... args) {
        if (labelRepository.count() == 0) {
            Label label = new Label();
            label.setName("feature");
            Label label1 = new Label();
            label1.setName("bug");
            labelRepository.save(label);
            labelRepository.save(label1);
        }
    }
}
