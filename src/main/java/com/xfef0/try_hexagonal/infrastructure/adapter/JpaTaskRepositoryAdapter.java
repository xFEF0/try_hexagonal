package com.xfef0.try_hexagonal.infrastructure.adapter;

import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import com.xfef0.try_hexagonal.infrastructure.entity.TaskEntity;
import com.xfef0.try_hexagonal.infrastructure.repository.JpaTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = TaskEntity.fromDomainModel(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return savedTaskEntity.toDomainModel();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id)
                .map(TaskEntity::toDomainModel);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskEntity::toDomainModel)
                .toList();
    }

    @Override
    public Optional<Task> update(Task task) {
        Optional<Task> updatedTask = Optional.empty();
        if (taskRepository.existsById(task.getId())) {
            TaskEntity newTaskEntity = TaskEntity.fromDomainModel(task);
            Optional<TaskEntity> taskEntity = taskRepository.findById(task.getId())
                    .map(oldTaskEntity -> {
                        oldTaskEntity.setTitle(newTaskEntity.getTitle());
                        oldTaskEntity.setDescription(newTaskEntity.getDescription());
                        oldTaskEntity.setComplete(newTaskEntity.isComplete());
                        return oldTaskEntity;
                    });
            if (taskEntity.isPresent()) {
                TaskEntity updatedTaskEntity = taskRepository.save(taskEntity.get());
                updatedTask = Optional.of(updatedTaskEntity.toDomainModel());
            }
        }
        return updatedTask;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleted = false;
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }
}
