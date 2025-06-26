package org.example.backend.exceptions.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.exceptions.custom.EntityNullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling for all controllers.
 * Maps different types of exceptions to appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions that occur when request parameters
     * fail validation constraints.
     * 
     * @param ex the validation exception
     * @return ResponseEntity with validation error details and BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles EntityNullException when an entity is not found.
     * 
     * @param ex the EntityNullException
     * @return ResponseEntity with error message and NOT_FOUND status
     */
    @ExceptionHandler(EntityNullException.class)
    public ResponseEntity<Map<String, String>> handleEntityNullException(EntityNullException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles EntityBorrowedException when an entity cannot be deleted
     * because it is currently in use.
     * 
     * @param ex the EntityBorrowedException
     * @return ResponseEntity with error message and CONFLICT status
     */
    @ExceptionHandler(EntityBorrowedException.class)
    public ResponseEntity<Map<String, String>> handleEntityBorrowedException(EntityBorrowedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles general exceptions that are not caught by specific handlers.
     * 
     * @param ex the general exception
     * @return ResponseEntity with error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
