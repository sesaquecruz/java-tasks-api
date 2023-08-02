package com.task.api.domain.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Auth0IdentifierTest {
    @Test
    public void shouldCreateAnIdentifierWithUniqueValue() {
        var id1 = Auth0Identifier.unique();
        var id2 = Auth0Identifier.unique();
        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    public void shouldCreateAnIdentifierWhenValueIsAnUUID() {
        var uuid = UUID.randomUUID().toString();
        var auth0Id = "auth0|%s".formatted(uuid.replace("-", "").substring(0, 24));
        var id = Auth0Identifier.with(auth0Id);
        assertThat(id.getValue()).isEqualTo(auth0Id);
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Auth0Identifier.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("auth0_id")).isTrue();
                    assertThat(handler.getMessages("auth0_id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("auth0_id").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Auth0Identifier.with("        "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("auth0_id")).isTrue();
                    assertThat(handler.getMessages("auth0_id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("auth0_id").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> Auth0Identifier.with("23IUOai123092313huymh"))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("auth0_id")).isTrue();
                    assertThat(handler.getMessages("auth0_id").size()).isEqualTo(1);
                    assertThat(handler.getMessages("auth0_id").get(0)).isEqualTo("is invalid");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareIdentifiersWithSameValue() {
        var uuid = UUID.randomUUID().toString();
        var auth0Id = "auth0|%s".formatted(uuid.replace("-", "").substring(0, 24));
        var id1 = Auth0Identifier.with(auth0Id);
        var id2 = Auth0Identifier.with(auth0Id);
        assertThat(id1.equals(id2)).isTrue();
    }
}
