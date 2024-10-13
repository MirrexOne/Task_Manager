package hexlet.code.mappers;

import hexlet.code.dto.TaskDto;
import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskStatusMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@RequiredArgsConstructor
public abstract class TaskMapper {

    protected TaskStatusRepository taskStatusRepository;

    protected UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "taskStatusId")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(TaskDto.Request dto);

    @Mapping(target = "taskStatus", source = "taskStatus.name")
    @Mapping(target = "assignee", source = "assignee.email")
    public abstract TaskDto.Response map(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "taskStatusId")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract void update(TaskDto.Request dto, @MappingTarget Task task);

    @Autowired
    public void setTaskStatusRepository(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected TaskStatus mapTaskStatus(Long taskStatusId) {
        if (taskStatusId == null) {
            return null;
        }
        return taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new RuntimeException("TaskStatus not found"));
    }

    protected User mapUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
