package com.task.api.domain.task.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriorityTest {
    @Test
    public void shouldCreateAPriorityWhenValueIsLow() {
        assertThat(Priority.with("low").getValue()).isEqualTo("LOW");
        assertThat(Priority.with("LOW").getValue()).isEqualTo("LOW");
    }

    @Test
    public void shouldCreateAPriorityWhenValueIsNormal() {
        assertThat(Priority.with("normal").getValue()).isEqualTo("NORMAL");
        assertThat(Priority.with("NORMAL").getValue()).isEqualTo("NORMAL");
    }

    @Test
    public void shouldCreateAPriorityWhenValueIsHigh() {
        assertThat(Priority.with("high").getValue()).isEqualTo("HIGH");
        assertThat(Priority.with("HIGH").getValue()).isEqualTo("HIGH");
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Priority.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Priority.with("          "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> Priority.with("LowNormalHigh"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("priority")).isTrue();
                    assertThat(handler.getMessages("priority").size()).isEqualTo(1);
                    assertThat(handler.getMessages("priority").get(0))
                            .isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldBeEqualWhenComparePrioritiesWithSameValue() {
        var value = "High";
        var priority1 = Priority.with(value);
        var priority2 = Priority.with(value);
        assertThat(priority1.equals(priority2)).isTrue();
    }
}
