package com.task.api.application.task.delete;

public record DeleteTaskInput(
        String taskId,
        String userId
) {
    public static DeleteTaskInput with(String taskId, String userId) {
        return new DeleteTaskInput(taskId, userId);
    }
}
