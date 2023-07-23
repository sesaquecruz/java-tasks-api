package com.task.api.domain.task;

import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.Validator;
import com.task.api.domain.validation.ErrorHandler;

public class TaskValidator extends Validator<Task> {
    private static final String CAUSE_ID = "id";
    private static final String CAUSE_USER_ID = "user id";
    private static final String CAUSE_NAME = "name";
    private static final String CAUSE_DESCRIPTION = "description";
    private static final String CAUSE_PRIORITY = "priority";
    private static final String CAUSE_STATUS = "status";
    private static final String CAUSE_DUE_DATE = "due date";
    private static final String CAUSE_CREATED_AT = "created at";
    private static final String CAUSE_UPDATED_AT = "updated at";

    private static final String MESSAGE_NOT_NULL = "must not be null";

    private TaskValidator(Task task, ErrorHandler handler) {
        super(task, handler);
    }

    public static TaskValidator with(Task task) {
        return new TaskValidator(task, ErrorHandler.create());
    }

    @Override
    public void validate() {
        var task = getEntity();
        var handler = getHandler();

        if (task.getId() == null)
            handler.addError(CAUSE_ID, MESSAGE_NOT_NULL);

        if (task.getUserId() == null)
            handler.addError(CAUSE_USER_ID, MESSAGE_NOT_NULL);

        if (task.getName() == null)
            handler.addError(CAUSE_NAME, MESSAGE_NOT_NULL);

        if (task.getDescription() == null)
            handler.addError(CAUSE_DESCRIPTION, MESSAGE_NOT_NULL);

        if (task.getPriority() == null)
            handler.addError(CAUSE_PRIORITY, MESSAGE_NOT_NULL);

        if (task.getStatus() == null)
            handler.addError(CAUSE_STATUS, MESSAGE_NOT_NULL);

        if (task.getDueDate() == null)
            handler.addError(CAUSE_DUE_DATE, MESSAGE_NOT_NULL);

        if (task.getCreatedAt() == null)
            handler.addError(CAUSE_CREATED_AT, MESSAGE_NOT_NULL);

        if (task.getUpdatedAt() == null)
            handler.addError(CAUSE_UPDATED_AT, MESSAGE_NOT_NULL);

        if (handler.hasError())
            throw ValidationException.with(handler);
    }
}