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

    public static Task getTask(TaskGateway gateway, Identifier taskId, Identifier userId) {
        Optional<Task> task;
        try {
            task = gateway.findById(taskId, userId);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }

        if (task.isEmpty())
            throw NotFoundException.with(Task.class, taskId);

        return task.get();
    }
}
