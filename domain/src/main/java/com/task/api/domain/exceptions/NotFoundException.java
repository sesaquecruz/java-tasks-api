package com.task.api.domain.exceptions;

import com.task.api.domain.Entity;
import com.task.api.domain.valueobjects.Identifier;

public class NotFoundException extends DomainException {
    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException with(
            Class<? extends Entity> entity,
            Identifier id
    ) {
        var message = "%s with id %s was not found"
                .formatted(entity.getSimpleName(), id.getValue())
                .toLowerCase();
        return new NotFoundException(message);
    }
}
