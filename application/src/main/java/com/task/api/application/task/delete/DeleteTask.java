package com.task.api.application.task.delete;

import com.task.api.application.UseCase;
import com.task.api.domain.task.TaskGateway;

public abstract class DeleteTask extends UseCase<DeleteTaskInput, Void> {
    protected final TaskGateway taskGateway;

    protected DeleteTask(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }
}
