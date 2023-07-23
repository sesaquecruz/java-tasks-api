package com.task.api.domain.task.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;

public class Description extends ValueObject {
    private static final String CAUSE_DESCRIPTION = "description";

    private final String value;

    private Description(String value) {
        this.value = value;
    }

    public static Description with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_DESCRIPTION, "must not be null");
            throw ValidationException.with(handler);
        }

        var strippedValue = value.strip();

        if (strippedValue.isBlank()) {
            handler.addError(CAUSE_DESCRIPTION, "must not be blank");
            throw ValidationException.with(handler);
        }

        if (strippedValue.length() > 50) {
            handler.addError(CAUSE_DESCRIPTION, "must not have more than 50 characters");
            throw ValidationException.with(handler);
        }

        return new Description(strippedValue);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
