package com.task.api.infrastructure.task;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskMySQLGateway implements TaskGateway {
    private final TaskRepository taskRepository;

    public TaskMySQLGateway(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository
                .saveAndFlush(TaskJpaEntity.from(task))
                .toAggregate();
    }

    @Override
    public Optional<Task> findById(final Identifier id) {
        return taskRepository
                .findById(id.getValue())
                .map(TaskJpaEntity::toAggregate);
    }
}
