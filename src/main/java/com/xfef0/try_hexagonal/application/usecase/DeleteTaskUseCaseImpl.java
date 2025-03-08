package com.xfef0.try_hexagonal.application.usecase;

import com.xfef0.try_hexagonal.domain.port.in.DeleteTaskUseCase;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public boolean deleteTask(Long id) {
        return taskRepositoryPort.deleteById(id);
    }
}
