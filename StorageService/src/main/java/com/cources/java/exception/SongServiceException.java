package com.cources.java.exception;


import com.cources.java.exception.error.ErrorResponse;

public class SongServiceException extends CustomRuntimeException{

    public SongServiceException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
