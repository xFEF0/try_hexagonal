package com.xfef0.try_hexagonal.domain.port.in;

import com.xfef0.try_hexagonal.domain.model.Task;

public interface CreateTaskUseCase {

    Task createTask(Task task);
}
