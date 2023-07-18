package com.task.api.domain.validation;

import com.task.api.domain.Entity;

import static java.util.Objects.requireNonNull;

public abstract class EntityValidator<T extends Entity> {
    private final T entity;
    private final ValidationHandler handler;

    protected EntityValidator(T entity, ValidationHandler handler) {
        this.entity = requireNonNull(entity);
        this.handler = requireNonNull(handler);
    }

    public abstract void validate();

    public T getEntity() {
        return entity;
    }

    public ValidationHandler getHandler() {
        return handler;
    }
}
