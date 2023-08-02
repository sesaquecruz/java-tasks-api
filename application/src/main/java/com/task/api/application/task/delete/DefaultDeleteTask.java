package com.task.api.application.task.delete;

import com.task.api.domain.task.TaskGateway;

import static com.task.api.application.utils.IdentifierUtils.buildAuth0Identifier;
import static com.task.api.application.utils.IdentifierUtils.buildIdentifier;
import static com.task.api.application.utils.TaskUtils.getTask;

public class DefaultDeleteTask extends DeleteTask {
    public DefaultDeleteTask(final TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public Void execute(final DeleteTaskInput input) {
        var taskId = buildIdentifier(input.taskId());
        var userId = buildAuth0Identifier(input.userId());
        getTask(taskGateway, taskId, userId);
        taskGateway.delete(taskId, userId);
        return null;
    }
}
