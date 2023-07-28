package com.task.api.application.task.updated;

public record UpdateTaskInput(
        String taskId,
        String userId,
        String name,
        String description,
        String priority,
        String status,
        String dueDate
) {
    public static UpdateTaskInput with(
            String taskId,
            String userId,
            String name,
            String description,
            String priority,
            String status,
            String dueDate
    ) {
        return new UpdateTaskInput(
                taskId,
                userId,
                name,
                description,
                priority,
                status,
                dueDate
        );
    }
}
