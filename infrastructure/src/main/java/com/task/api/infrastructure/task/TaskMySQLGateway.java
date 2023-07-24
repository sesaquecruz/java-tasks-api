package com.task.api.infrastructure.task;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import org.springframework.stereotype.Service;

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
}
