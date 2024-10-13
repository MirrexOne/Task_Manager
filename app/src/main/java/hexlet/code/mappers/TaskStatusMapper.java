package hexlet.code.mappers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entities.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TaskStatusMapper {

    TaskStatus map(TaskStatusDto.Request dto);

    TaskStatusDto.Response map(TaskStatus entity);

    void update(TaskStatusDto.Request dto, @MappingTarget TaskStatus entity);
}
