package com.task.api.application.utils;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.exceptions.NotFoundException;
import com.task.api.domain.task.Task;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.valueobjects.Identifier;

import java.util.Optional;

public final class TaskUtils {
    private TaskUtils() { }

    public static Task saveTask(TaskGateway gateway, Task task) {
        try {
            return gateway.save(task);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }
    }

    public static Task getTask(TaskGateway gateway, Identifier id) {
        Optional<Task> task;
        try {
            task = gateway.findById(id);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }

        if (task.isEmpty())
            throw NotFoundException.with(Task.class, id);

        return task.get();
    }

    public static void validateTaskOwner(Task task, Identifier ownerId) {
        if (!task.getUserId().equals(ownerId))
            throw NotFoundException.with(Task.class, task.getId());
    }
}
