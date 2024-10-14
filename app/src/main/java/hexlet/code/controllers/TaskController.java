package hexlet.code.controllers;

import hexlet.code.dto.TaskParams;
import hexlet.code.dto.TaskDto;
import hexlet.code.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto.Response> updateById(@PathVariable Long id, @RequestBody TaskDto.Request taskRequest) {
        TaskDto.Response updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping
    public ResponseEntity<TaskDto.Response> save(@RequestBody TaskDto.Request taskRequest) {
        TaskDto.Response createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto.Response> findById(@PathVariable Long id) {
        TaskDto.Response task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto.Response>> findAll(TaskParams taskParams) {
        List<TaskDto.Response> tasks = taskService.findAll(taskParams);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tasks.size()));
        return new ResponseEntity<>(tasks, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
