package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

public class TaskDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer index;
        @NotBlank
        @Size(min = 1)
        private String name;
        private String description;
        @Getter
        private Long taskStatusId;
        @Getter
        private Long assigneeId;
        private Set<Long> labelIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Integer index;
        private String name;
        private String description;
        private String taskStatus;
        private String assignee;
        private LocalDate createdAt;
        private Set<LabelDto.Response> labels;
    }
}
