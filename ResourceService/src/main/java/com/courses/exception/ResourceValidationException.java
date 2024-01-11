package com.courses.exception;

import com.courses.exception.error.ErrorResponse;

public class ResourceValidationException extends CustomRuntimeException{

    public ResourceValidationException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
