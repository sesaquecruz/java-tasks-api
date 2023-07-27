package com.task.api.infrastructure.task.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskJpaEntity, String> {
    Page<TaskJpaEntity> findAll(Specification<TaskJpaEntity> where, Pageable page);
}
