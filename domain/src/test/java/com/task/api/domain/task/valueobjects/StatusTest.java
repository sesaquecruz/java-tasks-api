package com.task.api.domain.task.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatusTest {
    @Test
    public void shouldCreateAStatusWhenValueIsPending() {
        assertThat(Status.with("pending").getValue()).isEqualTo("PENDING");
        assertThat(Status.with("PENDING").getValue()).isEqualTo("PENDING");
    }

    @Test
    public void shouldCreateAStatusWhenValueIsCompleted() {
        assertThat(Status.with("completed").getValue()).isEqualTo("COMPLETED");
        assertThat(Status.with("COMPLETED").getValue()).isEqualTo("COMPLETED");
    }

    @Test
    public void shouldCreateAStatusWhenValueIsCancelled() {
        assertThat(Status.with("cancelled").getValue()).isEqualTo("CANCELLED");
        assertThat(Status.with("CANCELLED").getValue()).isEqualTo("CANCELLED");
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Status.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Status.with("          "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> Status.with("PendingCompletedCancelled"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("status")).isTrue();
                    assertThat(handler.getMessages("status").size()).isEqualTo(1);
                    assertThat(handler.getMessages("status").get(0))
                            .isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareStatusWithSameValue() {
        var value = "Completed";
        var status1 = Status.with(value);
        var status2 = Status.with(value);
        assertThat(status1.equals(status2)).isTrue();
    }
}
