package com.task.api.infrastructure.task;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskQuery;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.PersistenceTest;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.*;
import java.util.stream.IntStream;

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

    @Test
    public void shouldReturnATaskWhenItsExists() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("Low"),
                Status.with("Cancelled"),
                Date.now()
        );

        repository.saveAndFlush(TaskJpaEntity.from(task));
        assertThat(repository.count()).isEqualTo(1);

        var savedTask = gateway.findById(task.getId()).get();

        assertThat(task).isEqualTo(savedTask);
        assertThat(task.getId()).isEqualTo(savedTask.getId());
        assertThat(task.getUserId()).isEqualTo(savedTask.getUserId());
        assertThat(task.getName()).isEqualTo(savedTask.getName());
        assertThat(task.getDescription()).isEqualTo(savedTask.getDescription());
        assertThat(task.getPriority()).isEqualTo(savedTask.getPriority());
        assertThat(task.getStatus()).isEqualTo(savedTask.getStatus());
        assertThat(task.getDueDate()).isEqualTo(savedTask.getDueDate());
        assertThat(task.getCreatedAt()).isEqualTo(savedTask.getCreatedAt());
        assertThat(task.getUpdatedAt()).isEqualTo(savedTask.getUpdatedAt());
    }

    @Test
    public void shouldReturnEmptyWhenTaskDoesNotExists() {
        var id = Identifier.unique();
        assertThat(repository.count()).isEqualTo(0);

        var savedTask = gateway.findById(id);
        assertThat(savedTask.isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnAllItemsWhenPageSizeIsGreaterThanTotal() {
        var tasks = saveTasks();
        tasks.sort(Comparator.comparing(t -> t.getName().getValue()));

        var query = TaskQuery.with(0, tasks.size(), null, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size());
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size());

        IntStream.range(0, tasks.size()).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnHalfItemsWhenPageSizeIsHalfOfTotal() {
        var tasks = saveTasks();
        tasks.sort(Comparator.comparing(t -> t.getName().getValue()));

        var query = TaskQuery.with(0, tasks.size()/2, null, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size()/2);
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size()/2);

        IntStream.range(0, tasks.size()/2).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnLastHalfItemsWhenPageNumberIsOneAndPageSizeIsHalfOfTotal() {
        var list = saveTasks();
        var tasks = list.stream()
                .sorted(Comparator.comparing(t -> t.getDueDate().getValue(), Comparator.reverseOrder()))
                .skip(list.size() / 2)
                .toList();

        var query = TaskQuery.with(1, list.size()/2, null, "dueDate", "desc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(1);
        assertThat(page.size()).isEqualTo(list.size()/2);
        assertThat(page.total()).isEqualTo(list.size());
        assertThat(page.items().size()).isEqualTo(list.size()/2);

        IntStream.range(0, tasks.size()/2).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnItemsThatNameContainsAnSpecificTerm() {
        var list = saveTasks();
        var term = "One";
        var tasks = list.stream()
                .sorted(Comparator.comparing(t -> t.getName().getValue()))
                .filter(t -> t.getName().getValue().contains(term) ||
                        t.getDescription().getValue().contains(term) ||
                        t.getPriority().getValue().contains(term) ||
                        t.getStatus().getValue().contains(term)
                )
                .toList();

        var query = TaskQuery.with(0, list.size(), term, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size());
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size());

        IntStream.range(0, tasks.size()).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnItemsThatDescriptionContainsAnSpecificTerm() {
        var list = saveTasks();
        var term = "Car";
        var tasks = list.stream()
                .sorted(Comparator.comparing(t -> t.getDescription().getValue()))
                .filter(t -> t.getName().getValue().contains(term) ||
                        t.getDescription().getValue().contains(term) ||
                        t.getPriority().getValue().contains(term) ||
                        t.getStatus().getValue().contains(term)
                )
                .toList();

        var query = TaskQuery.with(0, list.size(), term, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size());
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size());

        IntStream.range(0, tasks.size()).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnItemsThatPriorityContainsAnSpecificTerm() {
        var list = saveTasks();
        var term = "HIGH";
        var tasks = list.stream()
                .sorted(Comparator.comparing(t -> t.getPriority().getValue()))
                .filter(t -> t.getName().getValue().contains(term) ||
                        t.getDescription().getValue().contains(term) ||
                        t.getPriority().getValue().contains(term) ||
                        t.getStatus().getValue().contains(term)
                )
                .toList();

        var query = TaskQuery.with(0, list.size(), term, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size());
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size());

        IntStream.range(0, tasks.size()).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldReturnItemsThatStatusContainsAnSpecificTerm() {
        var list = saveTasks();
        var term = "PENDING";
        var tasks = list.stream()
                .sorted(Comparator.comparing(t -> t.getStatus().getValue()))
                .filter(t -> t.getName().getValue().contains(term) ||
                        t.getDescription().getValue().contains(term) ||
                        t.getPriority().getValue().contains(term) ||
                        t.getStatus().getValue().contains(term)
                )
                .toList();

        var query = TaskQuery.with(0, list.size(), term, "name", "asc");
        var page = gateway.findAll(query);

        assertThat(page.page()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(tasks.size());
        assertThat(page.total()).isEqualTo(tasks.size());
        assertThat(page.items().size()).isEqualTo(tasks.size());

        IntStream.range(0, tasks.size()).forEach(i -> {
            var task = tasks.get(i);
            var item = page.items().get(i);

            assertThat(task.getId()).isEqualTo(item.getId());
            assertThat(task.getUserId()).isEqualTo(item.getUserId());
            assertThat(task.getDescription()).isEqualTo(item.getDescription());
            assertThat(task.getPriority()).isEqualTo(item.getPriority());
            assertThat(task.getStatus()).isEqualTo(item.getStatus());
            assertThat(task.getDueDate()).isEqualTo(item.getDueDate());
            assertThat(task.getCreatedAt()).isEqualTo(item.getCreatedAt());
            assertThat(task.getUpdatedAt()).isEqualTo(item.getUpdatedAt());
        });
    }

    @Test
    public void shouldDeleteATask() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("High"),
                Status.with("Pending"),
                Date.with(TimeUtils.now().toString())
        );

        gateway.save(task);
        assertThat(repository.count()).isEqualTo(1);

        gateway.delete(task.getId());
        assertThat(repository.count()).isEqualTo(0);
    }

    private List<Task> saveTasks() {
        var tasks = new ArrayList<>(List.of(
                Task.newTask(
                        Identifier.unique(),
                        Name.with("One"),
                        Description.with("Car"),
                        Priority.with("Low"),
                        Status.with("Pending"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Two"),
                        Description.with("Car"),
                        Priority.with("Normal"),
                        Status.with("Pending"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Three"),
                        Description.with("Bike"),
                        Priority.with("Normal"),
                        Status.with("Cancelled"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Four"),
                        Description.with("Skate"),
                        Priority.with("High"),
                        Status.with("Completed"),
                        Date.now()
                )
        ));

        repository.saveAllAndFlush(
                tasks.stream()
                        .map(TaskJpaEntity::from)
                        .toList()
        );

        return  tasks;
    }
}
