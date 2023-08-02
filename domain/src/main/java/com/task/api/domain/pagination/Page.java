package com.task.api.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Page<T>(
    int page,
    int size,
    long total,
    List<T> items
) {
    public static <T> Page<T> with(
            int page,
            int size,
            long total,
            List<T> items
    ) {
        return new Page<>(page, size, total, items);
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        var result = items.stream()
                .map(mapper)
                .toList();

        return new Page<>(page, size, total, result);
    }
}
