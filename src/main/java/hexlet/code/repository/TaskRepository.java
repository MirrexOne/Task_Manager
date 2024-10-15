package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE t.id IN :taskIds")
    Set<Task> findByIdIn(@Param("taskIds") Set<Long> taskIds);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels LEFT JOIN FETCH "
            + "t.assignee LEFT JOIN FETCH t.taskStatus WHERE t.name = :name")
    Optional<Task> findByNameWithEagerUpload(@Param("name") String name);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels LEFT JOIN FETCH "
            + "t.assignee LEFT JOIN FETCH t.taskStatus WHERE t.id = :id")
    Optional<Task> findByIdWithEagerUpload(@Param("id") Long id);


    //@EntityGraph(attributePaths = {"labels", "assignee", "taskStatus"})
    Optional<Task> findByName(String name);

    @Query("SELECT t FROM Task AS t LEFT JOIN FETCH t.labels WHERE t.name=:name")
    Optional<Task> findByNameWithLabels(@Param("name") String name);

    @Query("SELECT t FROM Task AS t LEFT JOIN FETCH t.labels WHERE t.id=:id")
    Optional<Task> findByIdWithLabels(@Param("id") Long id);
}
