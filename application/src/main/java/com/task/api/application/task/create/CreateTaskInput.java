package com.task.api.application.task.create;

public record CreateTaskInput(
        String userId,
        String name,
        String description,
        String priority,
        String status,
        String dueDate
) {
    public static CreateTaskInput with(
            String userId,
            String name,
            String description,
            String priority,
            String status,
            String dueDate
    ) {
        return new CreateTaskInput(
                userId,
                name,
                description,
                priority,
                status,
                dueDate
        );
    }
}
