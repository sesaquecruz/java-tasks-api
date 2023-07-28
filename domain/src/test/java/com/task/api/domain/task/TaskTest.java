package com.task.api.domain.task;

import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskTest {
    @Test
    public void shouldCreateANewTaskWhenDataIsValid() {
        var userId = Identifier.unique();
        var name = Name.with("A name");
        var description = Description.with("A description");
        var priority = Priority.with("Normal");
        var status = Status.with("Pending");
        var dueDate = Date.with(TimeUtils.now().toString());

        var task = Task.newTask(userId, name, description, priority, status, dueDate);

        assertThat(task.getId()).isNotNull();
        assertThat(task.getUserId()).isEqualTo(userId);
        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getPriority()).isEqualTo(priority);
        assertThat(task.getStatus()).isEqualTo(status);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getCreatedAt()).isEqualTo(task.getUpdatedAt());
    }

    @Test
    public void shouldCreateATaskWhenDataIsValid() {
        var id = Identifier.unique();
        var userId = Identifier.unique();
        var name = Name.with("A name");
        var description = Description.with("A description");
        var priority = Priority.with("Normal");
        var status = Status.with("Pending");
        var dueDate = Date.with(TimeUtils.now().toString());
        var createdAt = Date.with(TimeUtils.now().toString());
        var updatedAt = Date.with(TimeUtils.now().toString());

        var task = Task.with(id, userId, name, description, priority, status, dueDate, createdAt, updatedAt);

        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getUserId()).isEqualTo(userId);
        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getPriority()).isEqualTo(priority);
        assertThat(task.getStatus()).isEqualTo(status);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
        assertThat(task.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    public void shouldThrowAValidationExceptionWhenDataIsNull() {
        assertThatThrownBy(() -> Task.newTask(null,null, null, null, null, null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(6);
                    assertThat(handler.hasCause("user id")).isTrue();
                    assertThat(handler.getMessages("user id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("user id").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("due date")).isTrue();
                    assertThat(handler.getMessages("due date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("due date").get(0)).isEqualTo("must not be null");
                });

        assertThatThrownBy(() -> Task.with(null, null, null, null, null, null, null, null, null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(9);
                    assertThat(handler.hasCause("id")).isTrue();
                    assertThat(handler.getMessages("id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("id").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("user id")).isTrue();
                    assertThat(handler.getMessages("user id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("user id").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("due date")).isTrue();
                    assertThat(handler.getMessages("due date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("due date").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("created at")).isTrue();
                    assertThat(handler.getMessages("created at").size()).isEqualTo(1);
                    assertThat(handler.getMessages("created at").get(0)).isEqualTo("must not be null");
                    assertThat(handler.hasCause("updated at")).isTrue();
                    assertThat(handler.getMessages("updated at").size()).isEqualTo(1);
                    assertThat(handler.getMessages("updated at").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareTasksWithSameId() {
        var id = Identifier.unique();

        var task1 = Task.with(
                id,
                Identifier.unique(),
                Name.with("A name"),
                Description.with("A description"),
                Priority.with("Normal"),
                Status.with("Pending"),
                Date.with(TimeUtils.now().toString()),
                Date.with(TimeUtils.now().toString()),
                Date.with(TimeUtils.now().toString())
        );

        var task2 = Task.with(
                id,
                Identifier.unique(),
                Name.with("Other name"),
                Description.with("Other description"),
                Priority.with("Low"),
                Status.with("Cancelled"),
                Date.with(TimeUtils.now().toString()),
                Date.with(TimeUtils.now().toString()),
                Date.with(TimeUtils.now().toString())
        );

        assertThat(task1).isEqualTo(task2);
    }

    @Test
    public void shouldUpdateTaskFields() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Name"),
                Description.with("A Description"),
                Priority.with("LOW"),
                Status.with("PENDING"),
                Date.now()
        );

        var taskId = task.getId();
        var userId = task.getUserId();
        var dueDate = task.getDueDate();
        var createdAt = task.getCreatedAt();
        var updatedAt = task.getUpdatedAt();

        task.updateName(Name.with("A New Name"))
                .updateDescription(Description.with("A New Description"))
                .updatePriority(Priority.with("NORMAL"))
                .updateStatus(Status.with("COMPLETED"))
                .updateDueDate(Date.now());

        assertThat(task.getId()).isEqualTo(taskId);
        assertThat(task.getUserId()).isEqualTo(userId);
        assertThat(task.getName().getValue()).isEqualTo("A New Name");
        assertThat(task.getDescription().getValue()).isEqualTo("A New Description");
        assertThat(task.getPriority().getValue()).isEqualTo("NORMAL");
        assertThat(task.getStatus().getValue()).isEqualTo("COMPLETED");
        assertThat(task.getDueDate().getValue().isAfter(dueDate.getValue())).isTrue();
        assertThat(task.getCreatedAt().getValue()).isEqualTo(createdAt.getValue());
        assertThat(task.getUpdatedAt().getValue().isAfter(updatedAt.getValue())).isTrue();
    }
}
