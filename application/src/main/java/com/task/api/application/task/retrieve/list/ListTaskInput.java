package com.task.api.application.task.retrieve.list;

public record ListTaskInput(
        int page,
        int size,
        String term,
        String sort,
        String direction
) {
    public static ListTaskInput with (
            int page,
            int size,
            String term,
            String sort,
            String direction
    ) {
        return new ListTaskInput(page, size, term, sort, direction);
    }
}
