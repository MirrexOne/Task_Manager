package hexlet.code.services.impl;

import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.entities.User;
import hexlet.code.exception.CustomException;
import hexlet.code.mappers.UserMapper;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new CustomException("User already exists");
        }

        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
