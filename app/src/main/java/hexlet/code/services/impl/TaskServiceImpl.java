package hexlet.code.services.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.entities.Task;
import hexlet.code.entities.TaskStatus;
import hexlet.code.entities.User;
import hexlet.code.entities.Label;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.TaskMapper;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDto.Response createTask(TaskDto.Request taskDto) {
        Task task = taskMapper.map(taskDto);
        setTaskStatusAndAssigneeAndLabels(task, taskDto);
        return taskMapper.map(taskRepository.save(task));
    }

    @Override
    public TaskDto.Response getTaskById(Long id) {
        return taskMapper.map(findTaskById(id));
    }

    @Override
    public List<TaskDto.Response> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto.Response updateTask(Long id, TaskDto.Request taskDto) {
        Task task = findTaskById(id);
        taskMapper.update(taskDto, task);
        setTaskStatusAndAssigneeAndLabels(task, taskDto);
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

    private void setTaskStatusAndAssigneeAndLabels(Task task, TaskDto.Request taskDto) {
        if (taskDto.getTaskStatusId() != null) {
            TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус задачи не найден"));
            task.setTaskStatus(taskStatus);
        }
        if (taskDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
            task.setAssignee(assignee);
        }
        if (taskDto.getLabelIds() != null) {
            Set<Label> labels = taskDto.getLabelIds().stream()
                    .map(id -> labelRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Метка не найдена")))
                    .collect(Collectors.toSet());
            task.setLabels(labels);
        }
    }
}
