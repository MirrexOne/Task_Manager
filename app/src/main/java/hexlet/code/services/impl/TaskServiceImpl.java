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
import hexlet.code.services.TaskService;
import lombok.RequiredArgsConstructor;
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
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private void setTaskStatusAndAssignee(Task task, TaskDto.Request taskDto) {
        if (taskDto.getTaskStatusId() != null) {
            TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task status not found"));
            task.setTaskStatus(taskStatus);
        }
        if (taskDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setAssignee(assignee);
        }
    }
}
