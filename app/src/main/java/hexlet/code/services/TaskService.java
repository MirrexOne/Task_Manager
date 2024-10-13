package hexlet.code.services;

import hexlet.code.dto.TaskDto;
import java.util.List;

public interface TaskService {

    TaskDto.Response createTask(TaskDto.Request taskDto);

    TaskDto.Response getTaskById(Long id);

    List<TaskDto.Response> getAllTasks();

    TaskDto.Response updateTask(Long id, TaskDto.Request taskDto);

    void deleteTask(Long id);
}
