package com.ash.project.blogpost.security;

import com.ash.project.blogpost.model.ErrorResponse;
import com.ash.project.blogpost.model.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class PostRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(PostNotFoundException exc) {

// create a StudentErrorResponse

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

// return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

// add another exception handler ... to catch any exception (catch all)

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {

// create a StudentErrorResponse
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

// return ResponseEntity
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
