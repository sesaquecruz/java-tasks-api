package com.task.api.application.task.retrieve.get;

import com.task.api.application.UseCase;
import com.task.api.domain.task.TaskGateway;

public abstract class GetTask extends UseCase<GetTaskInput, GetTaskOutput> {
    protected final TaskGateway taskGateway;

    protected GetTask(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }
}
