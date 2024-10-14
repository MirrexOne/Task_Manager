package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

public class TaskDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer index;

        @NotNull
        private Long assignee_id;

        @NotBlank
        private String title;

        private String content;

        @NotBlank
        private String status;

        private Set<Long> labelIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Integer index;
        private LocalDate createdAt;
        private Long assignee_id;
        private String title;
        private String content;
        private String status;
        private Set<Long> labelIds;
    }
}
