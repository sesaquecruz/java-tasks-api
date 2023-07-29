package com.task.api.infrastructure.task;

import com.task.api.domain.pagination.Page;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.TaskQuery;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import com.task.api.infrastructure.utils.SpecificationUtils;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Task save(Task task) {
        return taskRepository
                .saveAndFlush(TaskJpaEntity.from(task))
                .toAggregate();
    }

    @Override
    public Optional<Task> findById(Identifier taskId, Identifier userId) {
        return taskRepository
                .findByIdAndUserId(taskId.getValue(), userId.getValue())
                .map(TaskJpaEntity::toAggregate);
    }

    @Override
    public Page<Task> findAll(TaskQuery query, Identifier userId) {
        var page = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Direction.fromString(query.getDirection()), query.getSort())
        );

        var equalsUserId = SpecificationUtils.<TaskJpaEntity>equal("userId", userId.getValue());

        var containsTerm = Optional.ofNullable(query.getTerm())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils
                        .<TaskJpaEntity>like("name", term)
                        .or(like("description", term))
                        .or(like("priority", term))
                        .or(like("status", term))
                )
                .orElse(null);

        var result = taskRepository.findAll(Specification.where(equalsUserId).and(containsTerm), page);

        return Page.with(
                result.getNumber(),
                result.getNumberOfElements(),
                result.getTotalElements(),
                result.map(TaskJpaEntity::toAggregate).toList()
        );
    }

    @Override
    @Transactional
    public void delete(Identifier taskId, Identifier userId) {
        taskRepository.deleteByIdAndUserId(taskId.getValue(), userId.getValue());
    }
}
