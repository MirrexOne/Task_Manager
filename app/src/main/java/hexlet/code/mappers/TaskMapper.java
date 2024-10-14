package hexlet.code.mappers;

import hexlet.code.dto.TaskDto;
import hexlet.code.entities.Label;
import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskStatusMapper.class, LabelMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@RequiredArgsConstructor
public abstract class TaskMapper {

    protected TaskStatusRepository taskStatusRepository;
    protected UserRepository userRepository;
    protected LabelRepository labelRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "taskStatusId")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "labelIds")
    public abstract Task map(TaskDto.Request dto);

    @Mapping(target = "taskStatus", source = "taskStatus.name")
    @Mapping(target = "assignee", source = "assignee.email")
    public abstract TaskDto.Response map(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", source = "taskStatusId")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "labelIds")
    public abstract void update(TaskDto.Request dto, @MappingTarget Task task);

    @Autowired
    public void setTaskStatusRepository(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLabelRepository(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    protected TaskStatus mapTaskStatus(Long taskStatusId) {
        if (taskStatusId == null) {
            return null;
        }
        return taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new RuntimeException("TaskStatus не найден"));
    }

    protected User mapUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    protected Set<Label> mapLabels(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
        return labelIds.stream()
                .map(id -> labelRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Метка не найдена")))
                .collect(Collectors.toSet());
    }
}
