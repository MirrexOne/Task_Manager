package hexlet.code.config;

import hexlet.code.entities.Label;
import hexlet.code.entities.Task;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> titleContains(String title) {
        return (root, query, cb) -> {
            if (title == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.like(cb.lower(root.get("name")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, cb) -> {
            if (assigneeId == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("assignee").get("id"), assigneeId);
        };
    }

    public static Specification<Task> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("taskStatus").get("slug"), status);
        };
    }

    public static Specification<Task> hasLabel(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return cb.isTrue(cb.literal(true));
            }
            Join<Task, Label> labelJoin = root.join("labels", JoinType.LEFT);
            return cb.equal(labelJoin.get("id"), labelId);
        };
    }
}
