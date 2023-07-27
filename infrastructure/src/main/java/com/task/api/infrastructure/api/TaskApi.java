package com.task.api.infrastructure.api;

import com.task.api.domain.pagination.Page;
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
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TaskResponse> findTask(@PathVariable(name = "id") String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskResponse>> listTask(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "term", required = false, defaultValue = "") String term,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir
    );
}