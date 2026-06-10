package ru.adel.locationtracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.adel.locationtracker.public_interface.exception.CategoryAlreadyExistsException;
import ru.adel.locationtracker.public_interface.exception.CategoryNotFoundException;
import ru.adel.locationtracker.public_interface.exception.ErrorResponse;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CategoryNotFoundException e, HttpServletRequest request) {
        log.warn("Category not found: {}", e.getMessage());
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(CategoryAlreadyExistsException e, HttpServletRequest request) {
        log.warn("Category conflict: {}", e.getMessage());
        return build(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .message(message)
                        .path(request.getRequestURI())
                        .build()
        );
    }
}
