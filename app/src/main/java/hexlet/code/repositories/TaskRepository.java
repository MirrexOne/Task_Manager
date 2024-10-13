package hexlet.code.repositories;

import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByAssignee(User assignee);

    boolean existsByTaskStatus(TaskStatus taskStatus);
}
