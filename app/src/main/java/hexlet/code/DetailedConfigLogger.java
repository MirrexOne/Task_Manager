package hexlet.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DetailedConfigLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DetailedConfigLogger.class);

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) {
        logger.info("Active profiles: {}", String.join(", ", env.getActiveProfiles()));
        logger.info("Default profiles: {}", String.join(", ", env.getDefaultProfiles()));
        logger.info("Database URL: {}", env.getProperty("spring.datasource.url"));
        logger.info("Database Username: {}", env.getProperty("spring.datasource.username"));
        logger.info("Database Password is set: {}", (env.getProperty("spring.datasource.password") != null));
        logger.info("Database Driver: {}", env.getProperty("spring.datasource.driver-class-name"));
        logger.info("JPA Database Platform: {}", env.getProperty("spring.jpa.database-platform"));
        logger.info("JPA Show SQL: {}", env.getProperty("spring.jpa.show-sql"));
        logger.info("JPA Hibernate DDL Auto: {}", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    }
}