package com.task.api.application.task.updated;

import com.task.api.application.UseCase;
import com.task.api.domain.task.TaskGateway;

public abstract class UpdateTask extends UseCase<UpdateTaskInput, Void> {
    protected final TaskGateway taskGateway;

    protected UpdateTask(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }
}
