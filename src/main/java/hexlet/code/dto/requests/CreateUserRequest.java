package hexlet.code.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {

    @Email(message = "Email not valid")
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Firstname should not be blank")
    @Size(min = 3, max = 100)
    private String firstName;

    @NotBlank(message = "Lastname should not be blank")
    @Size(min = 3, max = 100)
    private String lastName;

    @NotBlank(message = "Password should not be blank")
    private String password;
}
