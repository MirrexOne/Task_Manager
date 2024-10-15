package hexlet.code.repositories;

import hexlet.code.entities.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @EntityGraph(attributePaths = {"labels", "taskStatus", "assignee"})
    Optional<Task> findById(Long id);

    @EntityGraph(attributePaths = {"labels", "taskStatus", "assignee"})
    List<Task> findAll(Specification<Task> spec);
}
