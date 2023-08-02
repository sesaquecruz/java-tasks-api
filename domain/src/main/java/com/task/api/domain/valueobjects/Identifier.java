package com.task.api.domain.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;
import java.util.UUID;

public class Identifier extends ValueObject {
    private static final String CAUSE_ID = "id";
    private final UUID value;

    private Identifier(UUID value) {
        this.value = value;
    }

    public static Identifier with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_ID, "must not be null");
            throw ValidationException.with(handler);
        }

        if (value.isBlank()) {
            handler.addError(CAUSE_ID, "must not be blank");
            throw ValidationException.with(handler);
        }

        try {
            return new Identifier(UUID.fromString(value));
        } catch (IllegalArgumentException ex) {
            handler.addError(CAUSE_ID, "is invalid");
            throw ValidationException.with(handler);
        }
    }

    public static Identifier unique() {
        return Identifier.with(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value.toString();
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
