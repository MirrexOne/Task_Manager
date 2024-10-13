package hexlet.code.mappers;

import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(CreateUserRequest createUserRequest);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromDto(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}