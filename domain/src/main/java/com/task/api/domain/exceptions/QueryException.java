package com.task.api.domain.exceptions;

import com.task.api.domain.validation.ErrorHandler;

public class QueryException extends ValidationException {

    private QueryException(ErrorHandler handler) {
        super(handler);
    }

    public static QueryException with(ErrorHandler handler) {
        return new QueryException(handler);
    }
}
