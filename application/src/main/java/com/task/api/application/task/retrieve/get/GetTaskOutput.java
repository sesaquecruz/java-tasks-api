package com.task.api.application.task.retrieve.get;

import com.task.api.domain.task.Task;

public record GetTaskOutput(
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
    public static GetTaskOutput with(Task task) {
        return new GetTaskOutput(
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
