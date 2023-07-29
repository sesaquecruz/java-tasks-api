package com.task.api.application.task.update;

import com.task.api.application.task.updated.DefaultUpdateTask;
import com.task.api.application.task.updated.UpdateTaskInput;
import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.IdentifierException;
import com.task.api.domain.exceptions.NotFoundException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskUpdateTest {
    @Mock
    private TaskGateway gateway;
    @InjectMocks
    private DefaultUpdateTask useCase;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void shouldUpdateTaskWhenDataIsValid() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("NORMAL"),
                Status.with("PENDING"),
                Date.now()
        );

        when(gateway.findById(task.getId(), task.getUserId()))
                .thenReturn(Optional.of(task));

        when(gateway.save(any()))
                .thenAnswer(returnsFirstArg());

        var input = UpdateTaskInput.with(
                task.getId().getValue(),
                task.getUserId().getValue(),
                "A New Task",
                "A New Description",
                "HIGH",
                "COMPLETED",
                TimeUtils.now().toString()
        );

        useCase.execute(input);

        verify(gateway, times(1)).save(argThat(t ->
                Objects.equals(input.taskId(), t.getId().getValue()) &&
                Objects.equals(input.userId(), t.getUserId().getValue()) &&
                Objects.equals(input.name(), t.getName().getValue()) &&
                Objects.equals(input.description(), t.getDescription().getValue()) &&
                Objects.equals(input.priority(), t.getPriority().getValue()) &&
                Objects.equals(input.status(), t.getStatus().getValue()) &&
                Objects.equals(input.dueDate(), t.getDueDate().getValue().toString())
        ));
    }

    @Test
    public void shouldThrowAValidationExceptionWhenDataIsInvalid() {
        var input = UpdateTaskInput.with(
                "dfo1238o123l1k",
                Identifier.unique().getValue(),
                "A New Task",
                "A New Description",
                "HIGH",
                "COMPLETED",
                TimeUtils.now().toString()
        );

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(IdentifierException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("invalid id");
                });
    }

    @Test
    public void shouldThrowANotFoundExceptionWhenUserIsNotTaskOwner() {
        var userId = Identifier.unique();
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("NORMAL"),
                Status.with("PENDING"),
                Date.now()
        );

        when(gateway.findById(task.getId(), userId))
                .thenReturn(Optional.empty());

        var input = UpdateTaskInput.with(
                task.getId().getValue(),
                userId.getValue(),
                "A New Task",
                "A New Description",
                "HIGH",
                "COMPLETED",
                TimeUtils.now().toString()
        );

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(NotFoundException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage())
                            .isEqualTo("task with id %s was not found".formatted(task.getId().getValue()));
                });
    }

    @Test
    public void shouldThrowAGatewayExceptionWhenGatewayThrowsAnException() {
        var taskId = Identifier.unique();
        var userId = Identifier.unique();

        doThrow(GatewayException.with(new RuntimeException("Internal error")))
                .when(gateway).findById(taskId, userId);

        var input = UpdateTaskInput.with(
                taskId.getValue(),
                userId.getValue(),
                "A New Task",
                "A New Description",
                "HIGH",
                "COMPLETED",
                TimeUtils.now().toString()
        );

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(GatewayException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Gateway error");
                    assertThat(ex.getCause().getCause().getMessage()).isEqualTo("Internal error");
                });
    }
}
