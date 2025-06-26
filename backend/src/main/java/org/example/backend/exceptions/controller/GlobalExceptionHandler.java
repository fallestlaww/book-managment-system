package org.example.backend.exceptions.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for all REST-controllers in system.
 * Catches and handles custom and common exceptions, converting them into HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, Exception.class})
    public ResponseEntity<Object> invalidRequestException(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityBorrowedException.class)
    public ResponseEntity<Object> handleConflict(Exception e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Builds response for every exception using HTTP status, made to reduce repetition in code
     * @param status HTTP status to be thrown
     * @param message message to return, often is exception message
     * @return {@link ResponseEntity} with a status code and message according to the parameters
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }
}
