package com.task.api.application.utils;

import com.task.api.domain.exceptions.ValidationException;
import com.task.api.domain.validation.ErrorHandler;

import java.util.function.Supplier;

public final class ValidationUtils {
    private ValidationUtils() { }

    public static <T> T catchErrors(Supplier<T> field, ErrorHandler handler) {
        try {
            return field.get();
        } catch (ValidationException ex) {
            handler.addErrors(ex.getHandler());
            return null;
        }
    }
}
