package com.task.api.application.task.retrieve.list;

import com.task.api.application.UseCase;
import com.task.api.domain.pagination.Page;
import com.task.api.domain.task.TaskGateway;

public abstract class ListTask extends UseCase<ListTaskInput, Page<ListTaskOutput>> {
    protected final TaskGateway taskGateway;

    protected ListTask(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }
}
