package hexlet.code.services.impl;

import hexlet.code.dto.TaskDto;
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
import hexlet.code.config.TaskSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public TaskDto.Response createTask(TaskDto.Request taskDto) {
        Task task = taskMapper.map(taskDto);
        setTaskStatusAndAssignee(task, taskDto);
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

    @Override
    public List<TaskDto.Response> getFilteredTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = Specification.where(TaskSpecifications.titleContains(titleCont))
                .and(TaskSpecifications.hasAssignee(assigneeId))
                .and(TaskSpecifications.hasStatus(status))
                .and(TaskSpecifications.hasLabel(labelId));

        return taskRepository.findAll(spec).stream()
                .map(taskMapper::map)
                .collect(Collectors.toList());
    }

    private void setTaskStatusAndAssignee(Task task, TaskDto.Request taskDto) {
        if (taskDto.getStatus() != null) {
            TaskStatus taskStatus = taskStatusRepository.findBySlug(taskDto.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус задачи не найден"));
            task.setTaskStatus(taskStatus);
        }
        if (taskDto.getAssignee_id() != null) {
            User assignee = userRepository.findById(taskDto.getAssignee_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
            task.setAssignee(assignee);
        }
    }
}
