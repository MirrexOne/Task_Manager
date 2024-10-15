package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query("SELECT l FROM Label l LEFT JOIN FETCH l.tasks WHERE l.name = :name")
    Optional<Label> findByNameWithEagerUpload(@Param("name") String name);

    @Query("SELECT l FROM Label l WHERE l.id IN :labelsIds")
    Set<Label> findByIdIn(@Param("labelsIds") Set<Long> labelsIds);

    @Query("SELECT l FROM Label l LEFT JOIN FETCH l.tasks WHERE l.id = :id")
    Optional<Label> findByIdWithEagerUpload(@Param("id") Long id);


    //@EntityGraph(attributePaths = {"tasks"})
    Optional<Label> findByName(String name);

}
