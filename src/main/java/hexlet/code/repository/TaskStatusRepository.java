package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long>, JpaSpecificationExecutor<TaskStatus> {

    Optional<TaskStatus> findBySlug(String slug);

    @Query("SELECT ts FROM TaskStatus ts LEFT JOIN FETCH ts.tasks WHERE ts.id = :id")
    Optional<TaskStatus> findByIdWithEagerUpload(@Param("id") Long id);

    @Query("SELECT ts FROM TaskStatus ts LEFT JOIN FETCH ts.tasks WHERE ts.slug = :slug")
    Optional<TaskStatus> findBySlugWithEagerUpload(@Param("slug") String slug);
}
