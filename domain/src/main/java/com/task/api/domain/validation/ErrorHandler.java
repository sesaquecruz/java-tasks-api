package com.task.api.domain.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorHandler {
    private final Map<String, List<String>> errors;

    private ErrorHandler(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public static ErrorHandler create() {
        return new ErrorHandler(new HashMap<>());
    }

    public ErrorHandler addError(String cause, String message) {
        if (!errors.containsKey(cause))
            errors.put(cause, new ArrayList<>());

        errors.get(cause).add(message);
        return this;
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public boolean hasCause(String cause) {
        return errors.containsKey(cause);
    }

    public List<String> getMessages(String cause) {
        return errors.get(cause);
    }
}
