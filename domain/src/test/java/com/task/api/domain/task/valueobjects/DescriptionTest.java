package com.task.api.domain.task.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DescriptionTest {
    @Test
    public void shouldCreateADescriptionWhenValueIsValid() {
        var value = "A Description";
        var description = Description.with(value);
        assertThat(description.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldStripValueWhenCreateADescription() {
        var value = "  A Description     ";
        var name = Description.with(value);
        assertThat(name.getValue()).isEqualTo("A Description");
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Description.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Description.with("          "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsGreaterThan50Characters() {
        var value = "dfiuoierjfdsfaiou187312897312hjkasdy781238sdahduiy1";
        assertThat(value.length()).isEqualTo(51);
        assertThatThrownBy(() -> Description.with(value))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("description")).isTrue();
                    assertThat(handler.getMessages("description").size()).isEqualTo(1);
                    assertThat(handler.getMessages("description").get(0))
                            .isEqualTo("must not have more than 50 characters");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareDescriptionsWithSameValue() {
        var value = "A Description";
        var description1 = Description.with(value);
        var description2 = Description.with(value);
        assertThat(description1.equals(description2)).isTrue();
    }
}
