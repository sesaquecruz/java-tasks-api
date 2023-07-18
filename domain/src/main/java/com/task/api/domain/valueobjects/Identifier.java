package com.task.api.domain.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ValidationError;
import com.task.api.domain.validation.ValidationHandler;

import java.util.Objects;
import java.util.UUID;

public class Identifier extends ValueObject {
    private final String value;

    private Identifier(String value) {
        this.value = value;
    }

    public static Identifier with(String value) {
        var handler = ValidationHandler.create();

        if (value == null) {
            handler.addError(ValidationError.with("must not be null", "id"));
            throw ValidationException.with(handler);
        }

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            handler.addError(ValidationError.with("is invalid", "id"));
            throw ValidationException.with(handler);
        }

        return new Identifier(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
