package com.task.api.domain.task.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;

public class Name extends ValueObject {
    private static final String CAUSE_NAME = "name";
    private final String value;

    private Name(String value) {
        this.value = value;
    }

    public static Name with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_NAME, "must not be null");
            throw ValidationException.with(handler);
        }

        var strippedValue = value.strip();

        if (strippedValue.isBlank()) {
            handler.addError(CAUSE_NAME, "must not be blank");
            throw ValidationException.with(handler);
        }

        if (strippedValue.length() > 50) {
            handler.addError(CAUSE_NAME, "must not have more than 50 characters");
            throw ValidationException.with(handler);
        }

        return new Name(strippedValue);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(getValue(), name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
