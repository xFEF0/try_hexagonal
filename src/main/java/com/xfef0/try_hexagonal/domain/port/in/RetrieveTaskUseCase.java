package com.xfef0.try_hexagonal.domain.port.in;

import com.xfef0.try_hexagonal.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface RetrieveTaskUseCase {

    Optional<Task> getTask(Long id);
    List<Task> getAllTasks();
}
