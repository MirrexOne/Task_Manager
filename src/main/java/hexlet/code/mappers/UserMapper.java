package hexlet.code.mappers;

import hexlet.code.dto.requests.CreateUserRequest;
import hexlet.code.dto.requests.UpdateUserRequest;
import hexlet.code.dto.responses.UserResponse;
import hexlet.code.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toEntity(CreateUserRequest userRequestDto);

    UserResponse toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UpdateUserRequest userRequestDto, @MappingTarget User user);
}
