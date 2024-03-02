package com.cources.java.exception;


import com.cources.java.exception.error.ErrorResponse;

public class EntityNotFoundException extends CustomRuntimeException {

    public EntityNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
