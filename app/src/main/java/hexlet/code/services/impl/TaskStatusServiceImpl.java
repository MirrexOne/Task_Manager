package hexlet.code.services.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entities.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.services.TaskStatusService;
import hexlet.code.mappers.TaskStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;

    @Override
    public TaskStatusDto.Response createTaskStatus(TaskStatusDto.Request dto) {
        TaskStatus taskStatus = taskStatusMapper.map(dto);
        return taskStatusMapper.map(taskStatusRepository.save(taskStatus));
    }

    @Override
    public TaskStatusDto.Response getTaskStatus(Long id) {
        return taskStatusMapper.map(taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found")));
    }

    @Override
    public List<TaskStatusDto.Response> getAllTaskStatuses() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatusDto.Response updateTaskStatus(Long id, TaskStatusDto.Request dto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found"));
        taskStatusMapper.update(dto, taskStatus);
        return taskStatusMapper.map(taskStatusRepository.save(taskStatus));
    }

    @Override
    public void deleteTaskStatus(Long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task status not found");
        }
        taskStatusRepository.deleteById(id);
    }
}
