package com.task.api.infrastructure.task.persistence;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "task")
@Table(name = "tasks")
public class TaskJpaEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 36)
    private String id;
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "description", nullable = false, length = 50)
    private String description;
    @Column(name = "priority", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Priority.Value priority;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status.Value status;
    @Column(name = "due_date", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant dueDate;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public TaskJpaEntity() { }

    private TaskJpaEntity(
            String id,
            String userId,
            String name,
            String description,
            Priority.Value priority,
            Status.Value status,
            Instant dueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TaskJpaEntity from(Task task) {
        return new TaskJpaEntity(
                task.getId().getValue(),
                task.getUserId().getValue(),
                task.getName().getValue(),
                task.getDescription().getValue(),
                Priority.Value.valueOf(task.getPriority().getValue()),
                Status.Value.valueOf(task.getStatus().getValue()),
                task.getDueDate().getValue(),
                task.getCreatedAt().getValue(),
                task.getUpdatedAt().getValue()
        );
    }

    public Task toAggregate() {
        return Task.with(
                Identifier.with(getId()),
                Identifier.with(getUserId()),
                Name.with(getName()),
                Description.with(getDescription()),
                Priority.with(getPriority().toString()),
                Status.with(getStatus().toString()),
                Date.with(getDueDate().toString()),
                Date.with(getCreatedAt().toString()),
                Date.with(getUpdatedAt().toString())
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority.Value getPriority() {
        return priority;
    }

    public void setPriority(Priority.Value priority) {
        this.priority = priority;
    }

    public Status.Value getStatus() {
        return status;
    }

    public void setStatus(Status.Value status) {
        this.status = status;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
