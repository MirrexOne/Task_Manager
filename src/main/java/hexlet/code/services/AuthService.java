package hexlet.code.services;

import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.LoginRequest;
import hexlet.code.dto.responses.UserResponse;

public interface AuthService {

    UserResponse createUser(CreateUserRequest createUserRequest);

    String login(LoginRequest loginRequest);
}
