package com.task.api.application.task.retrieve.list;

public record ListTaskInput(
        String userId,
        int page,
        int size,
        String term,
        String sort,
        String direction
) {
    public static ListTaskInput with (
            String userId,
            int page,
            int size,
            String term,
            String sort,
            String direction
    ) {
        return new ListTaskInput(userId, page, size, term, sort, direction);
    }
}
