package hexlet.code.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {

    @Email(message = "Email not valid")
    private String email;

    @Size(min = 3, max = 100, message = "First name should be between 3 and 100 characters")
    private String firstName;

    @Size(min = 3, max = 100, message = "Last name should be between 3 and 100 characters")
    private String lastName;

    private String password;
}
