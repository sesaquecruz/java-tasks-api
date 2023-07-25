package com.task.api.infrastructure.api;

import com.task.api.infrastructure.task.models.CreateTaskRequest;
import com.task.api.infrastructure.task.models.TaskResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("tasks")
public interface TaskApi {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> createTask(@RequestBody CreateTaskRequest body);

    @GetMapping(
            value = "{id}"
    )
    ResponseEntity<TaskResponse> findTask(@PathVariable String id);
}
