package com.xfef0.try_hexagonal.domain.port.out;

import com.xfef0.try_hexagonal.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {

    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    Optional<Task> update(Task task);
    boolean deleteById(Long id);
}
