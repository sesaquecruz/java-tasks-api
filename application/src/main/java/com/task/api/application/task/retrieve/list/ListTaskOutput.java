package com.task.api.application.task.retrieve.list;

import com.task.api.domain.task.Task;

public record ListTaskOutput(
        String id,
        String userId,
        String name,
        String description,
        String priority,
        String status,
        String dueDate,
        String createdAt,
        String updatedAt
) {
    public static ListTaskOutput with(Task task) {
        return new ListTaskOutput(
                task.getId().getValue(),
                task.getUserId().getValue(),
                task.getName().getValue(),
                task.getDescription().getValue(),
                task.getPriority().getValue(),
                task.getStatus().getValue(),
                task.getDueDate().getValue().toString(),
                task.getCreatedAt().getValue().toString(),
                task.getUpdatedAt().getValue().toString()
        );
    }
}
