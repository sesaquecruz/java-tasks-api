package com.task.api.application.task.create;

public record CreateTaskOutput(
        String id
) {
    public static CreateTaskOutput with(String id) {
        return new CreateTaskOutput(id);
    }
}
