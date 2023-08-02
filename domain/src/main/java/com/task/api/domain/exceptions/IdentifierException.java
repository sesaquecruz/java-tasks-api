package com.task.api.domain.exceptions;

public class IdentifierException extends DomainException {
    private IdentifierException(String message) {
        super(message);
    }

    public static IdentifierException create() {
        return new IdentifierException("invalid id");
    }
}
