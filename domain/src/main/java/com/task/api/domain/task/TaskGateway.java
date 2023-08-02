package com.task.api.domain.task;

import com.task.api.domain.pagination.Page;
import com.task.api.domain.valueobjects.Auth0Identifier;
import com.task.api.domain.valueobjects.Identifier;

import java.util.Optional;

public interface TaskGateway {
    Task save(Task task);
    Optional<Task> findById(Identifier taskId, Auth0Identifier userId);
    Page<Task> findAll(TaskQuery query, Auth0Identifier userId);
    void delete(Identifier taskId, Auth0Identifier userId);
}
