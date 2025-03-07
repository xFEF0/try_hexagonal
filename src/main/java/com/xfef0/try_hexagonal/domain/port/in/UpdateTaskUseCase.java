package com.xfef0.try_hexagonal.domain.port.in;

import com.xfef0.try_hexagonal.domain.model.Task;

import java.util.Optional;

public interface UpdateTaskUseCase {

    Optional<Task> updateTask(Long id, Task updateTask);
}
