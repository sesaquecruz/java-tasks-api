package com.task.api.application.task.retrieve;

import com.task.api.application.task.retrieve.list.DefaultListTask;
import com.task.api.application.task.retrieve.list.ListTaskInput;
import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.QueryException;
import com.task.api.domain.pagination.Page;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListTaskTest {
    @Mock
    private TaskGateway gateway;
    @InjectMocks
    private DefaultListTask useCase;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void shouldReturnAPageWithItemsWhenItemsExist() {
        var tasks = List.of(
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Task 1"),
                        Description.with("Description 1"),
                        Priority.with("Normal"),
                        Status.with("Pending"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Task 2"),
                        Description.with("Description 2"),
                        Priority.with("High"),
                        Status.with("Completed"),
                        Date.now()
                )
        );

        var page = Page.with(1, 2, 2, tasks);

        when(gateway.findAll(any()))
                .thenReturn(page);

        var input = ListTaskInput.with(1, 3, "car", "name", "asc");
        var output = useCase.execute(input);

        assertThat(output.page()).isEqualTo(page.page());
        assertThat(output.size()).isEqualTo(page.size());
        assertThat(output.total()).isEqualTo(page.total());
        assertThat(output.items().size()).isEqualTo(page.items().size()).isEqualTo(2);

        IntStream.range(0, page.items().size()).forEach(i -> {
            var task = page.items().get(i);
            var result = output.items().get(i);

            assertThat(result.id()).isEqualTo(task.getId().getValue());
            assertThat(result.name()).isEqualTo(task.getName().getValue());
            assertThat(result.userId()).isEqualTo(task.getUserId().getValue());
            assertThat(result.description()).isEqualTo(task.getDescription().getValue());
            assertThat(result.priority()).isEqualTo(task.getPriority().getValue());
            assertThat(result.status()).isEqualTo(task.getStatus().getValue());
            assertThat(result.dueDate()).isEqualTo(task.getDueDate().getValue().toString());
            assertThat(result.createdAt()).isEqualTo(task.getCreatedAt().getValue().toString());
            assertThat(result.updatedAt()).isEqualTo(task.getUpdatedAt().getValue().toString());
        });

        verify(gateway, times(1)).findAll(any());
        verify(gateway, times(1)).findAll(argThat(query ->
                Objects.equals(input.page(), query.getPage()) &&
                Objects.equals(input.size(), query.getSize()) &&
                Objects.equals(input.term(), query.getTerm()) &&
                Objects.equals(input.sort(), query.getSort()) &&
                Objects.equals(input.direction(), query.getDirection())
        ));
    }

    @Test
    public void shouldReturnAEmptyPageWhenItemsDoNotExist() {
        var page = Page.with(0, 0, 0, new ArrayList<Task>());

        when(gateway.findAll(any()))
                .thenReturn(page);

        var input = ListTaskInput.with(0, 1, "car", "name", "desc");
        var output = useCase.execute(input);

        assertThat(output.page()).isEqualTo(page.page());
        assertThat(output.size()).isEqualTo(page.size());
        assertThat(output.total()).isEqualTo(page.total());
        assertThat(output.items().size()).isEqualTo(page.items().size()).isEqualTo(0);

        verify(gateway, times(1)).findAll(any());
        verify(gateway, times(1)).findAll(argThat(query ->
                Objects.equals(input.page(), query.getPage()) &&
                        Objects.equals(input.size(), query.getSize()) &&
                        Objects.equals(input.term(), query.getTerm()) &&
                        Objects.equals(input.sort(), query.getSort()) &&
                        Objects.equals(input.direction(), query.getDirection())
        ));
    }

    @Test
    public void shouldThrowAQueryExceptionWhenInputIsInvalid() {
        var input = ListTaskInput.with(
                -1,
                0,
                "sdf912kjfiouf0192312jlkasduoi31",
                "description",
                "des"
        );

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(QueryException.class)
                .satisfies(ex -> {
                   var handler = ((QueryException) ex).getHandler();
                   assertThat(handler.hasError()).isTrue();
                   assertThat(handler.getErrors().size()).isEqualTo(5);
                   assertThat(handler.hasCause("page")).isTrue();
                   assertThat(handler.getMessages("page").size()).isEqualTo(1);
                   assertThat(handler.getMessages("page").get(0)).isEqualTo("must not be negative");
                   assertThat(handler.hasCause("size")).isTrue();
                   assertThat(handler.getMessages("size").size()).isEqualTo(1);
                   assertThat(handler.getMessages("size").get(0)).isEqualTo("must be greater than 0");
                   assertThat(handler.hasCause("term")).isTrue();
                   assertThat(handler.getMessages("term").size()).isEqualTo(1);
                   assertThat(handler.getMessages("term").get(0)).isEqualTo("must have less than 30 characters");
                   assertThat(handler.hasCause("sort")).isTrue();
                   assertThat(handler.getMessages("sort").size()).isEqualTo(1);
                   assertThat(handler.getMessages("sort").get(0)).isEqualTo("must be name or dueDate");
                   assertThat(handler.hasCause("direction")).isTrue();
                   assertThat(handler.getMessages("direction").size()).isEqualTo(1);
                   assertThat(handler.getMessages("direction").get(0)).isEqualTo("must be asc or desc");
                });
    }

    @Test
    public void shouldThrowAGatewayExceptionWhenGatewayThrowsAnException() {
        doThrow(GatewayException.with(new RuntimeException("Internal error")))
                .when(gateway).findAll(any());

        var input = ListTaskInput.with(0, 3, "car", "name", "asc");

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(GatewayException.class)
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Gateway error");
                    assertThat(ex.getCause().getCause().getMessage()).isEqualTo("Internal error");
                });
    }
}
