package com.task.api.domain.task;

import com.task.api.domain.exceptions.QueryException;
import com.task.api.domain.pagination.Query;
import com.task.api.domain.validation.ErrorHandler;

public class TaskQuery extends Query {
    private enum Sort { name, dueDate }

    private TaskQuery(
            int page,
            int size,
            String term,
            String sort,
            Query.Direction direction
    ) {
        super(page, size, term, sort, direction);
    }

    public static TaskQuery with(
            int page,
            int size,
            String term,
            String sort,
            String direction
    ) {
        var handler = ErrorHandler.create();

        if (page < 0)
            handler.addError(Query.PAGE, "must not be negative");

        if (size < 1)
            handler.addError(Query.SIZE, "must be greater than 0");

        if (term != null && term.length() > 30)
            handler.addError(Query.TERM, "must have less than 30 characters");

        try {
            Sort.valueOf(sort);
        } catch (IllegalArgumentException ex) {
            handler.addError(Query.SORT, "must be name or dueDate");
        }

        try {
            Query.Direction.valueOf(direction);
        } catch (IllegalArgumentException ex) {
            handler.addError(Query.DIRECTION, "must be asc or desc");
        }

        if (handler.hasError())
            throw QueryException.with(handler);

        return new TaskQuery(page, size, term, sort, Direction.valueOf(direction));
    }
}
