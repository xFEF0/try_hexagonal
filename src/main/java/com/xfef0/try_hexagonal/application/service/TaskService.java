package com.xfef0.try_hexagonal.application.service;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.domain.port.in.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskService implements CreateTaskUseCase, DeleteTaskUseCase,
        UpdateTaskUseCase, RetrieveTaskUseCase, GetAdditionalTaskInfoUseCase  {

    private final CreateTaskUseCase createTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final RetrieveTaskUseCase retrieveTaskUseCase;
    private final GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase;

    @Override
    public Task createTask(Task task) {
        return createTaskUseCase.createTask(task);
    }

    @Override
    public boolean deleteTask(Long id) {
        return deleteTaskUseCase.deleteTask(id);
    }

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        return getAdditionalTaskInfoUseCase.getAdditionalTaskInfo(id);
    }

    @Override
    public Optional<Task> getTask(Long id) {
        return retrieveTaskUseCase.getTask(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return retrieveTaskUseCase.getAllTasks();
    }

    @Override
    public Optional<Task> updateTask(Long id, Task updateTask) {
        return updateTaskUseCase.updateTask(id, updateTask);
    }
}
