package com.task.api.domain.validation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ValidationHandler {
    private final List<ValidationError> errors;

    private ValidationHandler(List<ValidationError> errors) {
        this.errors = errors;
    }

    public static ValidationHandler create() {
        return new ValidationHandler(new ArrayList<>());
    }

    public ValidationHandler addError(ValidationError error) {
        errors.add(requireNonNull(error));
        return this;
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
