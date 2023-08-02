package com.task.api.application.task.retrieve.get;

public record GetTaskInput(
        String taskId,
        String userId
) {
    public static GetTaskInput with(
            String taskId,
            String userId
    ) {
        return new GetTaskInput(taskId, userId);
    }
}
