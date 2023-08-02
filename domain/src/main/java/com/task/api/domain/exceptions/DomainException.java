package com.task.api.domain.exceptions;

public abstract class DomainException extends RuntimeException {
    protected DomainException(String message, Throwable cause) {
        super(message, cause, true, false);
    }

    protected DomainException(String message) {
        this(message, null);
    }

    protected DomainException() {
        this(null);
    }
}
