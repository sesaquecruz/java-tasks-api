package com.task.api.application.task.create;

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

import static com.task.api.application.utils.TaskUtils.saveTask;
import static com.task.api.application.utils.ValidationUtils.catchErrors;

public class DefaultCreateTask extends CreateTask {
    public DefaultCreateTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public CreateTaskOutput execute(CreateTaskInput input) {
        var task = buildTask(input);
        return CreateTaskOutput.with(saveTask(taskGateway, task));
    }

    public static Task buildTask(CreateTaskInput input) {
        var handler = ErrorHandler.create();

        var userId = catchErrors(() -> Identifier.with(input.userId()), handler);
        handler.changeCause("id", "user_id");

        var name = catchErrors(() -> Name.with(input.name()), handler);
        var description = catchErrors(() -> Description.with(input.description()), handler);
        var priority = catchErrors(() -> Priority.with(input.priority()), handler);
        var status = catchErrors(() -> Status.with(input.status()), handler);

        var dueDate = catchErrors(() -> Date.with(input.dueDate()), handler);
        handler.changeCause("date", "due_date");

        if (handler.hasError())
            throw ValidationException.with(handler);

        return Task.newTask(userId, name, description, priority, status, dueDate);
    }
}
