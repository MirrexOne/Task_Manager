package hexlet.code.services;

import hexlet.code.dto.TaskStatusDto;

import java.util.List;

public interface TaskStatusService {

    TaskStatusDto.Response createTaskStatus(TaskStatusDto.Request dto);

    TaskStatusDto.Response getTaskStatus(Long id);

    List<TaskStatusDto.Response> getAllTaskStatuses();

    TaskStatusDto.Response updateTaskStatus(Long id, TaskStatusDto.Request dto);

    void deleteTaskStatus(Long id);
}
