package com.task.api.domain.task.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;

public class Status extends ValueObject {
    public enum Value { PENDING, COMPLETED, CANCELLED }

    private static final String CAUSE_STATUS = "status";
    private final Value value;

    private Status(Value value) {
        this.value = value;
    }

    public static Status with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_STATUS, "must not be null");
            throw ValidationException.with(handler);
        }

        if (value.isBlank()) {
            handler.addError(CAUSE_STATUS, "must not be blank");
            throw ValidationException.with(handler);
        }

        try {
            return new Status(Status.Value.valueOf(value.toUpperCase()));
        } catch (Exception ex) {
            handler.addError(CAUSE_STATUS, "is invalid");
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
        Status status = (Status) o;
        return Objects.equals(getValue(), status.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
