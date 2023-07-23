package com.task.api.domain.task.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;

public class Priority extends ValueObject {
    private enum Value { LOW, NORMAL, HIGH }

    private static final String CAUSE_PRIORITY = "priority";
    private final Value value;

    private Priority(Value value) {
        this.value = value;
    }

    public static Priority with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_PRIORITY, "must not be null");
            throw ValidationException.with(handler);
        }

        if (value.isBlank()) {
            handler.addError(CAUSE_PRIORITY, "must not be blank");
            throw ValidationException.with(handler);
        }

        try {
            return new Priority(Value.valueOf(value.toUpperCase()));
        } catch (Exception ex) {
            handler.addError(CAUSE_PRIORITY, "is invalid");
            throw ValidationException.with(handler);
        }
    }

    public String getValue() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority = (Priority) o;
        return Objects.equals(getValue(), priority.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
