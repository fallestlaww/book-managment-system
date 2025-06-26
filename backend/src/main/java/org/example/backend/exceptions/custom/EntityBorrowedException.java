package org.example.backend.exceptions.custom;

/**
 * Custom exception thrown when an entity is currently borrowed or in use.
 * This exception is used to indicate that an entity cannot be deleted
 * or modified because it is currently being used by another entity.
 */
public class EntityBorrowedException extends RuntimeException {
    
    /**
     * Constructs a new EntityBorrowedException with the specified detail message.
     * 
     * @param message the detail message explaining the exception
     */
    public EntityBorrowedException(String message) {
        super(message);
    }
}
