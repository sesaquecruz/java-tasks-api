package com.task.api.domain.task;

import com.task.api.domain.pagination.Page;
import com.task.api.domain.valueobjects.Identifier;

import java.util.Optional;

public interface TaskGateway {
    Task save(Task task);
    Optional<Task> findById(Identifier id);
    Page<Task> findAll(TaskQuery query);
    void delete(Identifier id);
}
