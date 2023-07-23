package com.task.api.application.task.create;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.validation.ErrorHandler;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;

import java.util.function.Supplier;

public class DefaultCreateTask extends CreateTask {
    public DefaultCreateTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public CreateTaskOutput execute(CreateTaskInput input) {
        var task = buildTask(input);
        return buildOutput(saveTask(task));
    }

    private Task buildTask(CreateTaskInput input) {
        var handler = ErrorHandler.create();

        var userId = catchErrors(() -> Identifier.with(input.userId()), handler);
        handler.changeCause("id", "user id");

        var name = catchErrors(() -> Name.with(input.name()), handler);
        var description = catchErrors(() -> Description.with(input.description()), handler);
        var priority = catchErrors(() -> Priority.with(input.priority()), handler);
        var status = catchErrors(() -> Status.with(input.status()), handler);
        var dueDate = catchErrors(() -> Date.with(input.dueDate()), handler);

        if (handler.hasError())
            throw ValidationException.with(handler);

        return Task.newTask(userId, name, description, priority, status, dueDate);
    }

    private <T> T catchErrors(Supplier<T> field, ErrorHandler handler) {
        try {
            return field.get();
        } catch (ValidationException ex) {
            handler.addErrors(ex.getHandler());
            return null;
        }
    }

    private Task saveTask(Task task) {
        try {
            return taskGateway.save(task);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }
    }

    private CreateTaskOutput buildOutput(Task task) {
        return CreateTaskOutput.with(task.getId().getValue().toString());
    }
}
