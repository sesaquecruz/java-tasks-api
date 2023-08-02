package com.task.api.domain.validation;

import com.task.api.domain.Entity;

public abstract class Validator<T extends Entity> {
    private final T entity;
    private final ErrorHandler handler;

    protected Validator(T entity, ErrorHandler handler) {
        this.entity = entity;
        this.handler = handler;
    }

    public abstract void validate();

    protected T getEntity() {
        return entity;
    }

    protected ErrorHandler getHandler() {
        return handler;
    }
}
