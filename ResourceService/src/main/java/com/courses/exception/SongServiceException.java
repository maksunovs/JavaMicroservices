package com.courses.exception;

import com.courses.exception.error.ErrorResponse;

public class SongServiceException extends CustomRuntimeException{

    public SongServiceException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
