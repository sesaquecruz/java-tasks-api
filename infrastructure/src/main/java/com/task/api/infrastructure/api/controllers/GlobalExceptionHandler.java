package com.task.api.infrastructure.api.controllers;

import com.task.api.domain.exceptions.*;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(value = IdentifierException.class)
    public ResponseEntity<?> identifierException(IdentifierException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(value = QueryException.class)
    public ResponseEntity<?> queryException(QueryException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getHandler().getErrors());
    }

    @ExceptionHandler(value = GatewayException.class)
    public ResponseEntity<?> gatewayException(GatewayException ex) {
        return ResponseEntity
                .internalServerError()
                .body(ex.getMessage());
    }
}
