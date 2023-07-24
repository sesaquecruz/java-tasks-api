package com.task.api.domain.exceptions;

public abstract class InternalErrorException extends RuntimeException {
    protected InternalErrorException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
