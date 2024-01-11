package com.courses.exception;

import com.courses.exception.error.ErrorResponse;

public class EntityNotFoundException extends CustomRuntimeException {

    public EntityNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
