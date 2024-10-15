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
        Task task = taskMapper.toTask(taskRequest);
        setTaskStatusAndAssignee(task, taskRequest);
        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    @Override
    public TaskDto.Response getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public List<TaskDto.Response> findAll(TaskParams taskParams) {
        Specification<Task> specification = taskSpecifications.build(taskParams);
        return taskRepository.findAll(specification)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public TaskDto.Response updateTask(Long id, TaskDto.Request taskDto) {
        Task task = taskRepository.findById(id).orElseThrow();
        Task updated = taskMapper.partialUpdate(taskDto, task);
        setTaskStatusAndAssignee(updated, taskDto);
        Task saved = taskRepository.save(updated);
        return taskMapper.toTaskResponse(saved);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }

        taskRepository.deleteById(id);
    }

    private void setTaskStatusAndAssignee(Task task, TaskDto.Request taskRequest) {
        TaskStatus taskStatus = null;
        if (taskRequest.getStatus() != null) {
            taskStatus = taskStatusRepository.findBySlug(taskRequest.getStatus()).orElseThrow();
        }
        User user = null;
        if (taskRequest.getAssigneeId() != null) {
            user = userRepository.findById(taskRequest.getAssigneeId()).orElseThrow();
        }
        List<Label> labels = null;
        if (taskRequest.getLabelIds() != null) {
            labels = labelRepository.findAllById(taskRequest.getLabelIds());
        }
        task.setTaskStatus(taskStatus);
        task.setAssignee(user);
        task.setLabels(labels != null ? new HashSet<>(labels) : new HashSet<>());
    }
}
