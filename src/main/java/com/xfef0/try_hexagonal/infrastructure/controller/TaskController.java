package com.xfef0.try_hexagonal.infrastructure.controller;

import com.xfef0.try_hexagonal.application.service.TaskService;
import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> taskOptional = taskService.getTask(taskId);
        return ResponseEntity.of(taskOptional);
    }

    @GetMapping("/{taskId}/additionalInfo")
    public ResponseEntity<AdditionalTaskInfo> getAdditionalTaskInfo(@PathVariable Long taskId) {
        AdditionalTaskInfo additionalTaskInfo = taskService.getAdditionalTaskInfo(taskId);
        return ResponseEntity.of(Optional.ofNullable(additionalTaskInfo));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        Optional<Task> taskOptional = taskService.updateTask(taskId, task);
        return ResponseEntity.of(taskOptional);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        String baseResponse = "Task id = " + taskId;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(baseResponse + " not found.", HttpStatus.NOT_FOUND);
        if (taskService.deleteTask(taskId)) {
            responseEntity = new ResponseEntity<>(baseResponse + " deleted.", HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }
}
