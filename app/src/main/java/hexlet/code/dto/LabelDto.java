package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

public class LabelDto implements Serializable {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements Serializable {
        @NotBlank
        @Size(min = 3, max = 1000)
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response implements Serializable {
        private Long id;
        @Size(min = 3, max = 1000)
        private String name;
        private LocalDate createdAt;
    }
}
