package com.task.api.domain.exceptions;

public class GatewayException extends InternalErrorException {
    private GatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public static GatewayException with(Throwable cause) {
        return new GatewayException("Gateway error", cause);
    }
}
