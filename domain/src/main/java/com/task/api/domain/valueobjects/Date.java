package com.task.api.domain.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.validation.ErrorHandler;

import java.time.Instant;
import java.util.Objects;

public class Date extends ValueObject {
    private static final String CAUSE_DATE = "date";
    private final Instant value;

    private Date(Instant value) {
        this.value = value;
    }

    public static Date with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_DATE, "must not be null");
            throw ValidationException.with(handler);
        }

        if (value.isBlank()) {
            handler.addError(CAUSE_DATE, "must not be blank");
            throw ValidationException.with(handler);
        }

        if (!TimeUtils.isValidInstant(value)) {
            handler.addError(CAUSE_DATE, "is invalid");
            throw ValidationException.with(handler);
        }

        return new Date(Instant.parse(value));
    }

    public Instant getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return Objects.equals(getValue(), date.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
