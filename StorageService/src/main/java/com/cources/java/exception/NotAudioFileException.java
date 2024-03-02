package com.cources.java.exception;


import com.cources.java.exception.error.ErrorResponse;

public class NotAudioFileException extends CustomRuntimeException {
    public NotAudioFileException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
