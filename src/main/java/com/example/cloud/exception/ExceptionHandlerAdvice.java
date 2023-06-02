package com.example.cloud.exception;

import com.example.cloud.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Error> badRequestHandler(BadRequestException e) {
        Error error = new Error();
        error.setId(400);
        error.setMessage(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Error> unauthorizedHandler(UnauthorizedException e) {
        Error error = new Error();
        error.setId(401);
        error.setMessage(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Error> internalServerErrorHandler(InternalServerErrorException e) {
        Error error = new Error();
        error.setId(500);
        error.setMessage(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}