package hexlet.code.services.impl;

import hexlet.code.config.TaskSpecifications;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskParams;
import hexlet.code.entities.Label;
import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.TaskMapper;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecifications taskSpecifications;

    @Override
    public TaskDto.Response createTask(TaskDto.Request taskRequest) {
        Task task = taskMapper.map(taskRequest);
        setTaskStatusAndAssignee(task, taskRequest);
        Task saved = taskRepository.save(task);
        return taskMapper.map(saved);
    }

    @Override
    public TaskDto.Response getTaskById(Long id) {
        return taskMapper.map(findTaskById(id));
    }

    @Override
    public List<TaskDto.Response> findAll(TaskParams taskParams) {
        Specification<Task> specification = taskSpecifications.build(taskParams);
        return taskRepository.findAll(specification).stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto.Response updateTask(Long id, TaskDto.Request taskDto) {
        Task task = findTaskById(id);
        taskMapper.update(taskDto, task);
        setTaskStatusAndAssignee(task, taskDto);
        return taskMapper.map(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Задача не найдена");
        }
        taskRepository.deleteById(id);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
    }

    private void setTaskStatusAndAssignee(Task task, TaskDto.Request taskDto) {
        if (taskDto.getStatus() != null) {
            TaskStatus taskStatus = taskStatusRepository.findBySlug(taskDto.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус задачи не найден"));
            task.setTaskStatus(taskStatus);
        }
        if (taskDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
            task.setAssignee(assignee);
        }
    }
}
