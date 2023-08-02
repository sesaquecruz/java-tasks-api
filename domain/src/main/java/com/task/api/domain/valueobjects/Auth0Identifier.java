package com.task.api.domain.valueobjects;

import com.task.api.domain.ValueObject;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Auth0Identifier extends ValueObject {
    private static final String CAUSE_AUTH0_ID = "auth0_id";
    private static final Pattern auth0IdPattern = Pattern.compile("^auth0\\|[a-fA-F0-9]{24}$");
    private final String value;

    private Auth0Identifier(String value) {
        this.value = value;
    }

    public static Auth0Identifier with(String value) {
        var handler = ErrorHandler.create();

        if (value == null) {
            handler.addError(CAUSE_AUTH0_ID, "must not be null");
            throw ValidationException.with(handler);
        }

        if (value.isBlank()) {
            handler.addError(CAUSE_AUTH0_ID, "must not be blank");
            throw ValidationException.with(handler);
        }

        if (!auth0IdPattern.matcher(value).matches()) {
            handler.addError(CAUSE_AUTH0_ID, "is invalid");
            throw ValidationException.with(handler);
        }

        return new Auth0Identifier(value);
    }

    public static Auth0Identifier unique() {
        var uuid = UUID.randomUUID().toString();
        var auth0Id = "auth0|%s".formatted(uuid.replace("-", "").substring(0, 24));
        return Auth0Identifier.with(auth0Id);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auth0Identifier that = (Auth0Identifier) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
