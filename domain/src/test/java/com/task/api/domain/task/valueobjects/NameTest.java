package com.task.api.domain.task.valueobjects;

import com.task.api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {
    @Test
    public void shouldCreateANameWhenValueIsValid() {
        var value = "A Name";
        var name = Name.with(value);
        assertThat(name.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldStripValueWhenCreateAName() {
        var value = "  A Name     ";
        var name = Name.with(value);
        assertThat(name.getValue()).isEqualTo("A Name");
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> Name.with(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0)).isEqualTo("must not be null");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsBlank() {
        assertThatThrownBy(() -> Name.with("          "))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0)).isEqualTo("must not be blank");
                });
    }

    @Test
    public void shouldThrowAValidationExceptionWhenValueIsGreaterThan50Characters() {
        var value = "dfiuoierjfdsfaiou187312897312hjkasdy781238sdahduiy1";
        assertThat(value.length()).isEqualTo(51);
        assertThatThrownBy(() -> Name.with(value))
                .isInstanceOf(ValidationException.class)
                .satisfies(ex -> {
                    var handler = ((ValidationException) ex).getHandler();
                    assertThat(handler.hasError()).isTrue();
                    assertThat(handler.getErrors().size()).isEqualTo(1);
                    assertThat(handler.hasCause("name")).isTrue();
                    assertThat(handler.getMessages("name").size()).isEqualTo(1);
                    assertThat(handler.getMessages("name").get(0))
                            .isEqualTo("must not have more than 50 characters");
                });
    }

    @Test
    public void shouldBeEqualWhenCompareNamesWithSameValue() {
        var value = "A Name";
        var name1 = Name.with(value);
        var name2 = Name.with(value);
        assertThat(name1.equals(name2)).isTrue();
    }
}
