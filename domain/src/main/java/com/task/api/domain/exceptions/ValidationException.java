package com.task.api.domain.exceptions;

import com.task.api.domain.validation.ValidationHandler;

import static java.util.Objects.requireNonNull;

public class ValidationException extends DomainException {
    private final ValidationHandler handler;

    private ValidationException(ValidationHandler handler) {
        this.handler = handler;
    }

    public static ValidationException with(ValidationHandler handler) {
        return new ValidationException(requireNonNull(handler));
    }

    public ValidationHandler getHandler() {
        return handler;
    }
}
