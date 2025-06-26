package org.example.backend.exceptions.custom;

/**
 * Custom exception thrown when an entity is null or not found.
 * This exception is used to indicate that a requested entity
 * does not exist in the system.
 */
public class EntityNullException extends RuntimeException {
    
    /**
     * Constructs a new EntityNullException with the specified detail message.
     * 
     * @param message the detail message explaining the exception
     */
    public EntityNullException(String message) {
        super(message);
    }
}
