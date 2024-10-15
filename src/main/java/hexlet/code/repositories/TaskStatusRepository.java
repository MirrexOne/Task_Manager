package hexlet.code.repositories;

import hexlet.code.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    Optional<TaskStatus> findBySlug(String slug);

    Boolean existsBySlug(String slug);
}
