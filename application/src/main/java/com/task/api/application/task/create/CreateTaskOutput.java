package com.task.api.application.task.create;

import com.task.api.domain.task.Task;

public record CreateTaskOutput(
        String id
) {
    public static CreateTaskOutput with(Task task) {
        return new CreateTaskOutput(task.getId().getValue());
    }
}
