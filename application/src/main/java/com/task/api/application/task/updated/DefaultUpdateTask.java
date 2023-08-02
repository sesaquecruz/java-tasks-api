package com.task.api.application.task.updated;

import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.validation.ErrorHandler;
import com.task.api.domain.valueobjects.Date;

import static com.task.api.application.utils.IdentifierUtils.buildAuth0Identifier;
import static com.task.api.application.utils.IdentifierUtils.buildIdentifier;
import static com.task.api.application.utils.TaskUtils.*;
import static com.task.api.application.utils.ValidationUtils.catchErrors;

public class DefaultUpdateTask extends UpdateTask {
    public DefaultUpdateTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public Void execute(UpdateTaskInput input) {
        var taskId = buildIdentifier(input.taskId());
        var userId = buildAuth0Identifier(input.userId());
        var task = getTask(taskGateway, taskId, userId);
        var updatedTask = updateTask(task, input);
        saveTask(taskGateway, updatedTask);
        return null;
    }

    public static Task updateTask(Task task, UpdateTaskInput input) {
        var handler = ErrorHandler.create();

        catchErrors(() -> task.updateName(Name.with(input.name())), handler);
        catchErrors(() -> task.updateDescription(Description.with(input.description())), handler);
        catchErrors(() -> task.updatePriority(Priority.with(input.priority())), handler);
        catchErrors(() -> task.updateStatus(Status.with(input.status())), handler);

        catchErrors(() -> task.updateDueDate(Date.with(input.dueDate())), handler);
        handler.changeCause("date", "due_date");

        if (handler.hasError())
            throw ValidationException.with(handler);

        return task;
    }
}
