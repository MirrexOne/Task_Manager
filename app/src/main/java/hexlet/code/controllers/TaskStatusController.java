package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.services.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskStatusDto.Response> createTaskStatus(@Valid @RequestBody TaskStatusDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskStatusService.createTaskStatus(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusDto.Response> getTaskStatus(@PathVariable Long id) {
        return ResponseEntity.ok(taskStatusService.getTaskStatus(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusDto.Response>> getAllTaskStatuses() {
        return ResponseEntity.ok(taskStatusService.getAllTaskStatuses());
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskStatusDto.Response> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusDto.Request request) {
        return ResponseEntity.ok(taskStatusService.updateTaskStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
