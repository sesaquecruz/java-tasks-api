package com.task.api.domain;

import com.task.api.domain.valueobjects.Identifier;

public abstract class AggregateRoot extends Entity {
    protected AggregateRoot(Identifier id) {
        super(id);
    }
}
