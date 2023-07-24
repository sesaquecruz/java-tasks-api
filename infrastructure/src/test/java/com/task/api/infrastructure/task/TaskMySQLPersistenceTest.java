package com.task.api.infrastructure.task;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.PersistenceTest;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.assertj.core.api.Assertions.assertThat;

@PersistenceTest
public class TaskMySQLPersistenceTest {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private TaskMySQLGateway gateway;

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("tasks")
            .withUsername("root")
            .withPassword("root123");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL.getMappedPort(3306));
    }

    @BeforeEach
    public void beforeEach() {
        repository.deleteAll();
    }

    @Test
    public void shouldSaveATask() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("High"),
                Status.with("Pending"),
                Date.with(TimeUtils.now().toString())
        );

        assertThat(repository.count()).isEqualTo(0);
        var result = gateway.save(task);
        assertThat(repository.count()).isEqualTo(1);
        var savedTask = repository.findById(task.getId().getValue()).get().toAggregate();

        assertThat(task).isEqualTo(result).isEqualTo(savedTask);
        assertThat(task.getId()).isEqualTo(result.getId()).isEqualTo(savedTask.getId());
        assertThat(task.getUserId()).isEqualTo(result.getUserId()).isEqualTo(savedTask.getUserId());
        assertThat(task.getName()).isEqualTo(result.getName()).isEqualTo(savedTask.getName());
        assertThat(task.getDescription()).isEqualTo(result.getDescription()).isEqualTo(savedTask.getDescription());
        assertThat(task.getPriority()).isEqualTo(result.getPriority()).isEqualTo(savedTask.getPriority());
        assertThat(task.getStatus()).isEqualTo(result.getStatus()).isEqualTo(savedTask.getStatus());
        assertThat(task.getDueDate()).isEqualTo(result.getDueDate()).isEqualTo(savedTask.getDueDate());
        assertThat(task.getCreatedAt()).isEqualTo(result.getCreatedAt()).isEqualTo(savedTask.getCreatedAt());
        assertThat(task.getUpdatedAt()).isEqualTo(result.getUpdatedAt()).isEqualTo(savedTask.getUpdatedAt());
    }
}
