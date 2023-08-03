package com.task.api.infrastructure.api;

import com.task.api.domain.pagination.Page;
import com.task.api.infrastructure.task.models.CreateTaskRequest;
import com.task.api.infrastructure.task.models.DeleteTaskRequest;
import com.task.api.infrastructure.task.models.TaskResponse;
import com.task.api.infrastructure.task.models.UpdateTaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequestMapping("tasks")
public interface TaskApi {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create a task",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    ResponseEntity<Void> createTask(@Schema(hidden = true) JwtAuthenticationToken auth, @RequestBody CreateTaskRequest body);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Find a task",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid param", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    ResponseEntity<TaskResponse> findTask(@Schema(hidden = true) JwtAuthenticationToken auth, @PathVariable(name = "id") String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "List tasks",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid param", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    ResponseEntity<Page<TaskResponse>> listTask(
            @Schema(hidden = true) JwtAuthenticationToken auth,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "term", required = false, defaultValue = "") String term,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir
    );

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Update a task",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task updated"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    ResponseEntity<Void> updateTask(@Schema(hidden = true) JwtAuthenticationToken auth, @RequestBody UpdateTaskRequest body);

    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Delete a task",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    ResponseEntity<Void> deleteTask(@Schema(hidden = true) JwtAuthenticationToken auth, @RequestBody DeleteTaskRequest body);
}
