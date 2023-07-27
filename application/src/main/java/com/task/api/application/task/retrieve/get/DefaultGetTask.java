package com.task.api.application.task.retrieve.get;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.IdentifierException;
import com.task.api.domain.exceptions.NotFoundException;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.valueobjects.Identifier;

import java.util.Optional;

public class DefaultGetTask extends GetTask {
    public DefaultGetTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public GetTaskOutput execute(GetTaskInput input) {
        var id = buildIdentifier(input.id());
        var task = getTask(id);
        return GetTaskOutput.with(task);
    }

    private Identifier buildIdentifier(String id) {
        try {
            return Identifier.with(id);
        } catch (ValidationException ex) {
            throw IdentifierException.create();
        }
    }

    private Task getTask(Identifier id) {
        Optional<Task> task;
        try {
            task = taskGateway.findById(id);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }

        if (task.isEmpty())
            throw NotFoundException.with(Task.class, id);

        return task.get();
    }
}
