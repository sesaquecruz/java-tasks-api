package com.task.api.application.task.delete;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.NotFoundException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTaskTest {
    @Mock
    private TaskGateway gateway;
    @InjectMocks
    private DefaultDeleteTask useCase;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void shouldDeleteTaskWhenUserIsTaskOwner() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("NORMAL"),
                Status.with("PENDING"),
                Date.now()
        );

        when(gateway.findById(task.getId()))
                .thenReturn(Optional.of(task));

        doNothing()
                .when(gateway).delete(task.getId());

        var input = DeleteTaskInput.with(task.getId().getValue(), task.getUserId().getValue());
        useCase.execute(input);
    }

    @Test
    public void shouldThrowANotFoundExceptionTaskWhenUserIsNotTaskOwner() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("NORMAL"),
                Status.with("PENDING"),
                Date.now()
        );

        when(gateway.findById(task.getId()))
                .thenReturn(Optional.of(task));

        var input = DeleteTaskInput.with(task.getId().getValue(), Identifier.unique().getValue());
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(NotFoundException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage())
                            .isEqualTo("task with id %s was not found".formatted(task.getId().getValue()));
                });
    }

    @Test
    public void shouldThrowANotFoundExceptionTaskWhenTaskDoesNotExist() {
        var taskId = Identifier.unique();
        when(gateway.findById(taskId))
                .thenReturn(Optional.empty());

        var input = DeleteTaskInput.with(taskId.getValue(), Identifier.unique().getValue());
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(NotFoundException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage())
                            .isEqualTo("task with id %s was not found".formatted(taskId.getValue()));
                });
    }

    @Test
    public void shouldThrowAGatewayExceptionWhenGatewayThrowsAnException() {
        var taskId = Identifier.unique();
        doThrow(GatewayException.with(new RuntimeException("Internal error")))
                .when(gateway).findById(any());

        var input = DeleteTaskInput.with(taskId.getValue(), Identifier.unique().getValue());
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(GatewayException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Gateway error");
                    assertThat(ex.getCause().getCause().getMessage()).isEqualTo("Internal error");
                });
    }
}
