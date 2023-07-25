package com.task.api.infrastructure.api.controllers;

import com.task.api.application.task.create.CreateTask;
import com.task.api.application.task.create.CreateTaskInput;
import com.task.api.application.task.retrieve.get.GetTask;
import com.task.api.application.task.retrieve.get.GetTaskInput;
import com.task.api.infrastructure.api.TaskApi;
import com.task.api.infrastructure.task.models.CreateTaskRequest;
import com.task.api.infrastructure.task.models.TaskResponse;
import com.task.api.infrastructure.task.presenters.TaskApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class TaskController implements TaskApi {
    private final CreateTask createTaskUseCase;
    private final GetTask getTaskUseCase;

    public TaskController(
            CreateTask createTaskUseCase,
            GetTask getTaskUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
    }

    @Override
    public ResponseEntity<Void> createTask(CreateTaskRequest body) {
        var input = CreateTaskInput.with(
                body.userId(),
                body.name(),
                body.description(),
                body.priority(),
                body.status(),
                body.dueDate()
        );
        var output = createTaskUseCase.execute(input);
        var location = URI.create("/tasks/%s".formatted(output.id()));
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<TaskResponse> findTask(String id) {
        var input = GetTaskInput.with(id);
        var output = getTaskUseCase.execute(input);
        return ResponseEntity.ok(TaskApiPresenter.present(output));
    }
}
