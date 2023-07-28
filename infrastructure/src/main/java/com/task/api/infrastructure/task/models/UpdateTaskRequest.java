package com.task.api.infrastructure.task.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTaskRequest(
        @JsonProperty("id") String id,
        @JsonProperty("user_id") String userId,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("priority") String priority,
        @JsonProperty("status") String status,
        @JsonProperty("due_date") String dueDate
) {
}