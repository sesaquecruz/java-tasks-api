package com.task.api.application.task.create;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Auth0Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTaskTest {
    @Mock
    private TaskGateway gateway;
    @InjectMocks
    private DefaultCreateTask useCase;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void shouldCreateATaskWhenInputIsValid() {
        var input = CreateTaskInput.with(
                Auth0Identifier.unique().getValue(),
                "A Name",
                "A Description",
                "Normal",
                "Pending",
                TimeUtils.now().toString()
        );

        when(gateway.save(any()))
                .thenAnswer(returnsFirstArg());

        var output = useCase.execute(input);

        assertThat(output.id()).isNotNull();

        verify(gateway, times(1)).save(any());
        verify(gateway, times(1)).save(argThat(task ->
                Objects.equals(task.getId().getValue().toString(), output.id()) &&
                Objects.equals(task.getUserId().getValue().toString(), input.userId()) &&
                Objects.equals(task.getName().getValue(), input.name()) &&
                Objects.equals(task.getDescription().getValue(), input.description()) &&
                Objects.equals(task.getPriority().getValue(), input.priority().toUpperCase()) &&
                Objects.equals(task.getStatus().getValue(), input.status().toUpperCase()) &&
                Objects.equals(task.getDueDate().getValue().toString(), input.dueDate()) &&
                Objects.nonNull(task.getCreatedAt()) &&
                Objects.equals(task.getCreatedAt(), task.getUpdatedAt())
        ));
    }

    @Test
    public void shouldThrowAValidationExceptionWhenInputIsNull() {
        var input = CreateTaskInput.with(null, null, null, null, null, null);

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(6);
                    assertThat(handler.hasCause("user_id")).isTrue();
                    assertThat(handler.getMessages("user_id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("user_id").get(0)).isEqualTo("must not be null");
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
                    assertThat(handler.hasCause("due_date")).isTrue();
                    assertThat(handler.getMessages("due_date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("due_date").get(0)).isEqualTo("must not be null");
                });

        verify(gateway, times(0)).save(any());
    }

    @Test
    public void shouldThrowAValidationExceptionWhenInputIsBlank() {
        var input = CreateTaskInput.with("", "", "", "", "", "");

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(6);
                    assertThat(handler.hasCause("user_id")).isTrue();
                    assertThat(handler.getMessages("user_id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("user_id").get(0)).isEqualTo("must not be blank");
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0)).isEqualTo("must not be blank");
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0)).isEqualTo("must not be blank");
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0)).isEqualTo("must not be blank");
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0)).isEqualTo("must not be blank");
                    assertThat(handler.hasCause("due_date")).isTrue();
                    assertThat(handler.getMessages("due_date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("due_date").get(0)).isEqualTo("must not be blank");
                });

        verify(gateway, times(0)).save(any());
    }

    @Test
    public void shouldThrowAGatewayExceptionWhenGatewayThrowsAException() {
        var input = CreateTaskInput.with(
                Auth0Identifier.unique().getValue(),
                "A Name",
                "A Description",
                "High",
                "Completed",
                TimeUtils.now().toString()
        );

        doThrow(GatewayException.with(new RuntimeException("Internal error")))
                .when(gateway).save(any());

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(GatewayException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Gateway error");
                    assertThat(ex.getCause().getCause().getMessage()).isEqualTo("Internal error");
                });
    }
}
