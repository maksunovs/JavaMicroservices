package com.courses.exception;

import com.courses.exception.error.ErrorResponse;

public abstract class CustomRuntimeException extends RuntimeException{
    private final ErrorResponse errorResponse;
    public CustomRuntimeException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public CustomRuntimeException(ErrorResponse errorResponse, Throwable cause) {
        super(errorResponse.getMessage(), cause);
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
