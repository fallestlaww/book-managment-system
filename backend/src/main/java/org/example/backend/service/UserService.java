package org.example.backend.service;

import org.example.backend.dto.request.UserCreationRequest;
import org.example.backend.dto.request.UserUpdateRequest;
import org.example.backend.model.User;

/**
 * Service interface for user management operations.
 * Defines business logic methods for user CRUD operations.
 */
public interface UserService {
    /**
     * Creates a new user.
     * 
     * @param request the user creation request containing user data
     * @return the created user
     * @throws IllegalArgumentException if the request is null or user already exists
     */
    User createUser(UserCreationRequest request);
    
    /**
     * Retrieves a user by their ID.
     * 
     * @param id the user ID
     * @return the user with the specified ID
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    User readUser(Long id);
    
    /**
     * Updates an existing user.
     * 
     * @param id the user ID
     * @param request the user update request containing updated data
     * @return the updated user
     * @throws IllegalArgumentException if the request is null or user already exists
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    User updateUser(Long id, UserUpdateRequest request);
    
    /**
     * Deletes a user.
     * 
     * @param id the user ID
     * @throws jakarta.persistence.EntityNotFoundException if user is not found or has borrowed books
     */
    void deleteUser(Long id);
}
