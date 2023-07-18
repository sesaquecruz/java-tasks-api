package com.task.api.domain.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdentifierTest {
    @Test
    public void shouldCreateAnIdentifierWhenValueIsAnUUID() {
        var value = UUID.randomUUID().toString();
        var id = Identifier.with(value);
        assertThat(id.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowAValidationExceptionWhenTryCreateAnIdentifierWithANullValue() {
        assertThatThrownBy(() -> Identifier.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.getErrors().get(0).cause()).isEqualTo("id");
                    assertThat(handler.getErrors().get(0).message()).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenTryCreateAnIdentifierWithANonUUIDValue() {
        assertThatThrownBy(() -> Identifier.with("23IUOai123092313huymh"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.getErrors().get(0).cause()).isEqualTo("id");
                    assertThat(handler.getErrors().get(0).message()).isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldReturnTrueWhenCompareIdentifiersWithSameValue() {
        var value = UUID.randomUUID().toString();
        var id1 = Identifier.with(value);
        var id2 = Identifier.with(value);
        assertThat(id1.equals(id2)).isTrue();
    }
}
