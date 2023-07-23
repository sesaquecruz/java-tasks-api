package com.task.api.domain.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdentifierTest {
    @Test
    public void shouldCreateAnIdentifierWithUniqueValue() {
        var id1 = Identifier.unique();
        var id2 = Identifier.unique();
        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    public void shouldCreateAnIdentifierWhenValueIsAnUUID() {
        var value = UUID.randomUUID();
        var id = Identifier.with(value.toString());
        assertThat(id.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Identifier.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("id")).isTrue();
                    assertThat(handler.getMessages("id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("id").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Identifier.with("        "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("id")).isTrue();
                    assertThat(handler.getMessages("id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("id").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> Identifier.with("23IUOai123092313huymh"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("id")).isTrue();
                    assertThat(handler.getMessages("id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("id").get(0)).isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareIdentifiersWithSameValue() {
        var value = UUID.randomUUID().toString();
        var id1 = Identifier.with(value);
        var id2 = Identifier.with(value);
        assertThat(id1.equals(id2)).isTrue();
    }
}
