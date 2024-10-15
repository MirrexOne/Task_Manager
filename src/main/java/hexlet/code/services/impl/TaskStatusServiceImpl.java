package hexlet.code.services.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entities.TaskStatus;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.StatusMapper;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.services.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    private final StatusMapper statusMapper;

    @Override
    public TaskStatusDto.Response createTaskStatus(TaskStatusDto.Request dto) {
        TaskStatus taskStatus = statusMapper.toEntity(dto);
        TaskStatus saved = taskStatusRepository.save(taskStatus);
        return statusMapper.toDto(saved);
    }

    @Override
    public TaskStatusDto.Response getTaskStatus(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        return statusMapper.toDto(taskStatus);
    }

    @Override
    public List<TaskStatusDto.Response> getAllTaskStatuses() {
        return taskStatusRepository
                .findAll()
                .stream()
                .map(statusMapper::toDto)
                .toList();
    }

    @Override
    public TaskStatusDto.Response updateTaskStatus(Long id, TaskStatusDto.Request dto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
        TaskStatus updated = statusMapper.partialUpdate(dto, taskStatus);
        TaskStatus saved = taskStatusRepository.save(updated);
        return statusMapper.toDto(saved);
    }

    @Override
    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
