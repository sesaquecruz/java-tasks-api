package com.task.api.domain;

import com.task.api.domain.valueobjects.Identifier;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class Entity {
    private final Identifier id;

    protected Entity(Identifier id) {
        this.id = requireNonNull(id);
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
