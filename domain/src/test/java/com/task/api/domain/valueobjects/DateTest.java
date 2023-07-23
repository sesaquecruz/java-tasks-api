package com.task.api.domain.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DateTest {
    @Test
    public void shouldCreateADateWhenValueIsValid() {
        var value = TimeUtils.now().toString();
        var date = Date.with(value);
        assertThat(date.getValue()).isEqualTo(Instant.parse(value));
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Date.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("date")).isTrue();
                    assertThat(handler.getMessages("date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("date").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Date.with("          "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("date")).isTrue();
                    assertThat(handler.getMessages("date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("date").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> Date.with("09123094jhfi12"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("date")).isTrue();
                    assertThat(handler.getMessages("date").size()).isEqualTo(1);
                    assertThat(handler.getMessages("date").get(0)).isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareDatesWithSameValue() {
        var value = TimeUtils.now().toString();
        var dueDate1 = Date.with(value);
        var dueDate2 = Date.with(value);
        assertThat(dueDate1.equals(dueDate2)).isTrue();
    }
}
