package com.cources.java.exception;


import com.cources.java.exception.error.ErrorResponse;

public class ResourceValidationException extends CustomRuntimeException {

    public ResourceValidationException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
