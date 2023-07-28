package com.task.api.infrastructure.task;

import com.task.api.domain.pagination.Page;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.TaskQuery;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import com.task.api.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.task.api.infrastructure.utils.SpecificationUtils.like;

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

    @Override
    public Page<Task> findAll(TaskQuery query) {
        var page = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Direction.fromString(query.getDirection()), query.getSort())
        );

        var where = Optional.ofNullable(query.getTerm())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils
                        .<TaskJpaEntity>like("name", term)
                        .or(like("description", term))
                        .or(like("priority", term))
                        .or(like("status", term))
                )
                .orElse(null);

        var result = taskRepository.findAll(Specification.where(where), page);

        return Page.with(
                result.getNumber(),
                result.getNumberOfElements(),
                result.getTotalElements(),
                result.map(TaskJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public void delete(Identifier id) {
        taskRepository.deleteById(id.getValue());
    }
}
