package hexlet.code.mappers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entities.TaskStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusMapper {
    TaskStatusDto.Response toDto(TaskStatus taskStatus);

    TaskStatus toEntity(TaskStatusDto.Request taskStatusRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TaskStatus partialUpdate(TaskStatusDto.Request taskStatusRequest, @MappingTarget TaskStatus taskStatus);
}
