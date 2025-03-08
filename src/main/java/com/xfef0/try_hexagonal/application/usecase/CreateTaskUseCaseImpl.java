package com.xfef0.try_hexagonal.application.usecase;

import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.domain.port.in.CreateTaskUseCase;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateTaskUseCaseImpl implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        return taskRepositoryPort.save(task);
    }
}
