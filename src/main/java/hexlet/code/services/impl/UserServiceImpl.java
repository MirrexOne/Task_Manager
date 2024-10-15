package hexlet.code.services.impl;

import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.entities.User;
import hexlet.code.exception.CustomException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.UserMapper;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            updateUserRequest.setPassword(encodedPassword);
        }
        User user = userRepository.findById(id).orElseThrow();
        User updated = userMapper.partialUpdate(updateUserRequest, user);
        User saved = userRepository.save(updated);
        return userMapper.toDto(saved);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found");
        }
        userRepository.deleteById(id);
    }
}
