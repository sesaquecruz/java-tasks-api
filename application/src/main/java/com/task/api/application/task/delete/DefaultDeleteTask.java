package com.task.api.application.task.delete;

import com.task.api.domain.task.TaskGateway;

import static com.task.api.application.utils.IdentifierUtils.buildIdentifier;
import static com.task.api.application.utils.TaskUtils.getTask;
import static com.task.api.application.utils.TaskUtils.validateTaskOwner;

public class DefaultDeleteTask extends DeleteTask {
    public DefaultDeleteTask(final TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public Void execute(final DeleteTaskInput input) {
        var taskId = buildIdentifier(input.taskId());
        var userId = buildIdentifier(input.userId());
        var task = getTask(taskGateway, taskId);
        validateTaskOwner(task, userId);
        taskGateway.delete(taskId);
        return null;
    }
}
