package hexlet.code.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDateTime createdAt;
}