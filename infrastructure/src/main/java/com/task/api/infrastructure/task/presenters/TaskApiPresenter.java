package com.task.api.infrastructure.task.presenters;

import com.task.api.application.task.retrieve.get.GetTaskOutput;
import com.task.api.infrastructure.task.models.TaskResponse;

public interface TaskApiPresenter {
    static TaskResponse present(GetTaskOutput output) {
        return new TaskResponse(
                output.id(),
                output.name(),
                output.description(),
                output.priority(),
                output.status(),
                output.dueDate()
        );
    }
}
