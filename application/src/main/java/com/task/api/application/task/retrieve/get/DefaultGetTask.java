package com.task.api.application.task.retrieve.get;

import com.task.api.domain.task.TaskGateway;

import static com.task.api.application.utils.IdentifierUtils.buildIdentifier;
import static com.task.api.application.utils.TaskUtils.getTask;

public class DefaultGetTask extends GetTask {
    public DefaultGetTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public GetTaskOutput execute(GetTaskInput input) {
        var id = buildIdentifier(input.id());
        var task = getTask(taskGateway, id);
        return GetTaskOutput.with(task);
    }
}
