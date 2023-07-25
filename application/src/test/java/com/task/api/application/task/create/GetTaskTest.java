package com.task.api.application.task.create;

import com.task.api.application.task.retrieve.get.DefaultGetTask;
import com.task.api.application.task.retrieve.get.GetTaskInput;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTaskTest {
    @Mock
    private TaskGateway gateway;
    @InjectMocks
    private DefaultGetTask useCase;

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(gateway);
    }

    @Test
    public void shouldReturnATaskWhenIdExists() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("Normal"),
                Status.with("Completed"),
                Date.with(TimeUtils.now().toString())
        );

        when(gateway.findById(any()))
                .thenReturn(Optional.of(task));

        var input = GetTaskInput.with(task.getId().getValue());
        var output = useCase.execute(input);

        assertThat(output.id()).isEqualTo(task.getId().getValue());
        assertThat(output.name()).isEqualTo(task.getName().getValue());
        assertThat(output.description()).isEqualTo(task.getDescription().getValue());
        assertThat(output.priority()).isEqualTo(task.getPriority().getValue());
        assertThat(output.status()).isEqualTo(task.getStatus().getValue());
        assertThat(output.dueDate()).isEqualTo(task.getDueDate().getValue().toString());
        assertThat(output.createdAt()).isEqualTo(task.getCreatedAt().getValue().toString());
        assertThat(output.updatedAt()).isEqualTo(task.getUpdatedAt().getValue().toString());

        verify(gateway, times(1)).findById(any());
        verify(gateway, times(1)).findById(argThat(id ->
                Objects.equals(task.getId().getValue(), id.getValue())
        ));
    }

    @Test
    public void shouldThrowANotFoundExceptionWhenIdDoesNotExist() {
        var id = Identifier.unique();

        when(gateway.findById(any()))
                .thenReturn(Optional.empty());

        var input = GetTaskInput.with(id.getValue());

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(NotFoundException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage())
                            .isEqualTo("task with id %s was not found".formatted(id.getValue()));
                });

        verify(gateway, times(1)).findById(any());
        verify(gateway, times(1)).findById(argThat(arg ->
                Objects.equals(id.getValue(), arg.getValue())
        ));
    }

    @Test
    public void shouldThrowAIdentifierExceptionWhenIdIsNull() {
        var input = GetTaskInput.with(null);

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(IdentifierException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("invalid id");
                });

        verify(gateway, times(0)).findById(any());
    }

    @Test
    public void shouldThrowAIdentifierExceptionWhenIdIsBlank() {
        var id = "   ";
        var input = GetTaskInput.with(id);

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(IdentifierException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("invalid id");
                });

        verify(gateway, times(0)).findById(any());
    }

    @Test
    public void shouldThrowAGatewayExceptionWhenGatewayThrowsAnException() {
        var id = Identifier.unique();

        doThrow(GatewayException.with(new RuntimeException("Internal error")))
                .when(gateway).findById(any());

        var input = GetTaskInput.with(id.getValue());

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(GatewayException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Gateway error");
                    assertThat(ex.getCause().getCause().getMessage()).isEqualTo("Internal error");
                });
    }
}
