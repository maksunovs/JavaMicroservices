package com.courses.java.exception;

import com.courses.java.exception.error.ErrorResponse;

public class EntityNotFoundException extends CustomRuntimeException {

    public EntityNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
