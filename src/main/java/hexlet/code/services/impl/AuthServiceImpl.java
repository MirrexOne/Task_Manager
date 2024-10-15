package hexlet.code.services.impl;

import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.LoginRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.entities.User;
import hexlet.code.exception.CustomException;
import hexlet.code.mappers.UserMapper;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.AuthService;
import hexlet.code.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new CustomException("User already exists");
        }

        User user = userMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}
