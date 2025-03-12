package com.xfef0.try_hexagonal.infrastructure.adapter;

import com.xfef0.try_hexagonal.domain.model.Task;
import com.xfef0.try_hexagonal.infrastructure.repository.JpaTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JpaTaskRepositoryAdapterTest {

    @Autowired
    private JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter;
    @Autowired
    private JpaTaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldSaveTask() {
        Task task = getTask();

        Task savedTask = jpaTaskRepositoryAdapter.save(task);

        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(savedTask.getCreationDate()).isEqualTo(task.getCreationDate());
        assertThat(savedTask.isComplete()).isFalse();

    }

    @Test
    void shouldFindById() {
        Task task = getTask();

        Task savedTask = jpaTaskRepositoryAdapter.save(task);

        Optional<Task> foundTask = jpaTaskRepositoryAdapter.findById(savedTask.getId());

        assertThat(foundTask).isPresent();
        assertThat(foundTask.get())
                .usingRecursiveComparison()
                .withComparatorForType(JpaTaskRepositoryAdapterTest::compareLocalDateTime, LocalDateTime.class)
                .isEqualTo(savedTask);
    }

    @Test
    void shouldNotUpdate() {
        Task task = getTask();
        task.setId(1L);
        Optional<Task> foundTask = jpaTaskRepositoryAdapter.update(task);

        assertThat(foundTask).isEmpty();
    }

    @Test
    void testFindAll() {
        Task task1 = Task.builder()
                .title("Test Task 1")
                .description("encryptedMessage")
                .complete(false)
                .build();

        Task task2 = Task.builder()
                .title("Test Task 2")
                .description("This is a test task 2")
                .complete(true)
                .build();

        jpaTaskRepositoryAdapter.save(task1);
        jpaTaskRepositoryAdapter.save(task2);

        List<Task> tasks = jpaTaskRepositoryAdapter.findAll();

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Test Task 1", "Test Task 2");
    }

    @Test
    void testUpdateTask() {
        Task task = Task.builder()
                .title("Test Task")
                .description("This is a test task")
                .complete(false)
                .build();

        Task savedTask = jpaTaskRepositoryAdapter.save(task);

        Task updatedTask = Task.builder()
                .id(savedTask.getId())
                .title("Updated Test Task")
                .description("This is an updated test task")
                .complete(true)
                .build();

        Optional<Task> updated = jpaTaskRepositoryAdapter.update(updatedTask);

        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("Updated Test Task");
        assertThat(updated.get().getDescription()).isEqualTo("This is an updated test task");
        assertThat(updated.get().isComplete()).isTrue();
    }

    @Test
    void testDeleteById() {
        Task task = getTask();

        Task savedTask = jpaTaskRepositoryAdapter.save(task);
        assertThat(taskRepository.findById(savedTask.getId())).isNotEmpty();

        boolean deleted = jpaTaskRepositoryAdapter.deleteById(savedTask.getId());

        assertThat(deleted).isTrue();
        assertThat(taskRepository.findById(savedTask.getId())).isEmpty();
    }

    @Test
    void shouldNotDeleteById() {
        boolean deleted = jpaTaskRepositoryAdapter.deleteById(2L);

        assertThat(deleted).isFalse();
    }

    private static Task getTask() {
        return new Task(null, "title", "description", LocalDateTime.now(), false);
    }

    private static int compareLocalDateTime(LocalDateTime actual, LocalDateTime expected) {
        return actual.truncatedTo(ChronoUnit.MILLIS).compareTo(expected.truncatedTo(ChronoUnit.MILLIS));
    }

}