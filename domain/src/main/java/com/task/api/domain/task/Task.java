package com.task.api.domain.task;

import com.task.api.domain.AggregateRoot;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.utils.TimeUtils;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;

public class Task extends AggregateRoot {
    private final Identifier userId;
    private Name name;
    private Description description;
    private Priority priority;
    private Status status;
    private Date dueDate;
    private final Date createdAt;
    private Date updatedAt;

    private Task(
            Identifier id,
            Identifier userId,
            Name name,
            Description description,
            Priority priority,
            Status status,
            Date dueDate,
            Date createdAt,
            Date updatedAt
    ) {
        super(id);
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Task newTask(
            Identifier userId,
            Name name,
            Description description,
            Priority priority,
            Status status,
            Date dueDate
    ) {
        var now = TimeUtils.now().toString();
        var id = Identifier.unique();
        var createdAt = Date.with(now);
        var updatedAt = Date.with(now);

        return new Task(
                id,
                userId,
                name,
                description,
                priority,
                status,
                dueDate,
                createdAt,
                updatedAt
        ).validate();
    }

    public static Task with(
            Identifier id,
            Identifier userId,
            Name name,
            Description description,
            Priority priority,
            Status status,
            Date dueDate,
            Date createdAt,
            Date updatedAt
    ) {
        return new Task(
                id,
                userId,
                name,
                description,
                priority,
                status,
                dueDate,
                createdAt,
                updatedAt
        ).validate();
    }

    @Override
    protected Task validate() {
        TaskValidator.with(this).validate();
        return this;
    }

    public Task updateName(Name name) {
        this.name = name;
        this.updatedAt = Date.now();
        validate();
        return this;
    }

    public Task updateDescription(Description description) {
        this.description = description;
        this.updatedAt = Date.now();
        validate();
        return this;
    }

    public Task updatePriority(Priority priority) {
        this.priority = priority;
        this.updatedAt = Date.now();
        validate();
        return this;
    }

    public Task updateStatus(Status status) {
        this.status = status;
        this.updatedAt = Date.now();
        validate();
        return this;
    }

    public Task updateDueDate(Date dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = Date.now();
        validate();
        return this;
    }

    public Identifier getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
