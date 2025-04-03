package com.courses.handler;

import com.courses.exception.EntityNotFoundException;
import com.courses.exception.NotAudioFileException;
import com.courses.exception.SongServiceException;
import com.courses.exception.error.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    private static final Logger LOGGER = Logger.getLogger(RestResponseEntityExceptionHandler.class);
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotAudioFileException.class})
    public ResponseEntity<ErrorResponse> handleNotAudioFileException(
            NotAudioFileException ex) {
        logError(ex);
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SongServiceException.class})
    public ResponseEntity<ErrorResponse> handleSongServiceError(
            SongServiceException ex) {
        logError(ex);
        return new ResponseEntity<>(ex.getErrorResponse(), ex.getErrorResponse().getCode());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex) {
        logError(ex);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleGenericException(
            ConstraintViolationException ex) {
        logError(ex);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList().toString()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            Exception ex) {
        logError(ex);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, "Content-Type is not supported. Acceptable content type: multipart/form-data"), HttpStatus.BAD_REQUEST);
    }
    private void logError(Throwable ex) {
        LOGGER.error("Error: ",ex);
    }
}
