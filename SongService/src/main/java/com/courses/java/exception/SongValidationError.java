package com.courses.java.exception;

import com.courses.java.exception.error.ErrorResponse;

public class SongValidationError extends CustomRuntimeException {

    public SongValidationError(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
