package ru.adel.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.adel.apigateway.public_interface.exception.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid username or password!")
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnhandledExceptions(Exception e, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Unknown exception!")
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .build();
    }
}
