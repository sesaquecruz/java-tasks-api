package com.task.api.application.utils;

import com.task.api.domain.exceptions.IdentifierException;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.valueobjects.Identifier;

public final class IdentifierUtils {
    private IdentifierUtils() { }

    public static Identifier buildIdentifier(String id) {
        try {
            return Identifier.with(id);
        } catch (ValidationException ex) {
            throw IdentifierException.create();
        }
    }
}
