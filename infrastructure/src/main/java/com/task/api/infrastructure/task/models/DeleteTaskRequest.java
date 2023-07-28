package com.task.api.infrastructure.task.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteTaskRequest(
        @JsonProperty("task_id") String taskId,
        @JsonProperty("user_id") String userId
) {
}
