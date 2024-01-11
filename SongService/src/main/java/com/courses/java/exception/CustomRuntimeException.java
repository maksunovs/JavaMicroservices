package com.courses.java.exception;

import com.courses.java.exception.error.ErrorResponse;

public abstract class CustomRuntimeException extends RuntimeException{
    private ErrorResponse errorResponse;
    public CustomRuntimeException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
