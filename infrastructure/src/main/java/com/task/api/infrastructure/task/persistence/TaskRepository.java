package com.task.api.infrastructure.task.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskJpaEntity, String> {
    Optional<TaskJpaEntity> findByIdAndUserId(String id, String userId);
    Page<TaskJpaEntity> findAll(Specification<TaskJpaEntity> where, Pageable page);
    void deleteByIdAndUserId(String id, String userId);
}
