package com.xfef0.try_hexagonal.application.usecase;

import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.domain.port.in.RetrieveTaskUseCase;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RetrieveTaskUseCaseImpl implements RetrieveTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Optional<Task> getTask(Long id) {
        return taskRepositoryPort.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepositoryPort.findAll();
    }
}
