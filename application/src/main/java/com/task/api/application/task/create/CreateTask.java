package com.task.api.application.task.create;

import com.task.api.application.UseCase;
import com.task.api.domain.task.TaskGateway;

public abstract class CreateTask extends UseCase<CreateTaskInput, CreateTaskOutput> {
    protected final TaskGateway taskGateway;

    protected CreateTask(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }
}
