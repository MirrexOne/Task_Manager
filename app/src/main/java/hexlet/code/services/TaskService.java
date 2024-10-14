package hexlet.code.services;

import hexlet.code.dto.TaskDto;
import java.util.List;

public interface TaskService {

    TaskDto.Response createTask(TaskDto.Request taskDto);

    TaskDto.Response getTaskById(Long id);

    List<TaskDto.Response> getAllTasks();

    List<TaskDto.Response> getFilteredTasks(String titleCont, Long assigneeId, String status, Long labelId);

    TaskDto.Response updateTask(Long id, TaskDto.Request taskDto);

    void deleteTask(Long id);
}
