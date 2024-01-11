package com.courses.exception;

import com.courses.exception.error.ErrorResponse;

public class NotAudioFileException extends CustomRuntimeException{
    public NotAudioFileException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
