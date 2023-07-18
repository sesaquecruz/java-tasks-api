package com.task.api.domain.validation;

public record ValidationError(
        String message,
        String cause
) {
    public static ValidationError with(String message, String cause) {
        return new ValidationError(message, cause);
    }
}
