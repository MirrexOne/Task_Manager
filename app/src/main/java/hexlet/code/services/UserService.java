package hexlet.code.services;

import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);

    void deleteUser(Long id);
}
