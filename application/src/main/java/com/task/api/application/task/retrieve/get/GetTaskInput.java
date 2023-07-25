package com.task.api.application.task.retrieve.get;

public record GetTaskInput(
        String id
) {
    public static GetTaskInput with(String id) {
        return new GetTaskInput(id);
    }
}
