package com.cources.java.exception.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private HttpStatus code;
    private String message;

    public ErrorResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

}
