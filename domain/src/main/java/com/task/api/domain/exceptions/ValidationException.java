package com.task.api.domain.exceptions;

import com.task.api.domain.validation.ErrorHandler;

public class ValidationException extends DomainException {
    private final ErrorHandler handler;

    private ValidationException(ErrorHandler handler) {
        this.handler = handler;
    }

    public static ValidationException with(ErrorHandler handler) {
        return new ValidationException(handler);
    }

    public ErrorHandler getHandler() {
        return handler;
    }
}
