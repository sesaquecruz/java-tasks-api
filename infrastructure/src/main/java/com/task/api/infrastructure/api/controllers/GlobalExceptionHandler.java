package com.task.api.infrastructure.api.controllers;

import com.task.api.domain.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> validationException(ValidationException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(ex.getHandler().getErrors());
    }
}
