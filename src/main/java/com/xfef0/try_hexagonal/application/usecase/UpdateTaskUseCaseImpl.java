package com.xfef0.try_hexagonal.application.usecase;

import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.domain.port.in.UpdateTaskUseCase;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Optional<Task> updateTask(Long id, Task updateTask) {
        Optional<Task> taskOptional = taskRepositoryPort.findById(id);
        if (taskOptional.isEmpty()) {
            return taskOptional;
        } else {
            Task task = taskOptional.get();
            task.setTitle(updateTask.getTitle());
            task.setDescription(updateTask.getDescription());
            task.setComplete(updateTask.isComplete());
            return taskRepositoryPort.update(task);
        }
    }

}
