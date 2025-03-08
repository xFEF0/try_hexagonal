package com.xfef0.try_hexagonal.infrastructure.entity;

import com.xfef0.try_hexagonal.domain.model.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationTime;
    private boolean complete;

    public static TaskEntity fromDomainModel(Task  task) {
        return new TaskEntity(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreationDate(),
                task.isComplete()
        );
    }

    public Task toDomainModel() {
        return new Task(
                this.getId(),
                this.getTitle(),
                this.getDescription(),
                this.getCreationTime(),
                this.isComplete()
        );
    }
}
