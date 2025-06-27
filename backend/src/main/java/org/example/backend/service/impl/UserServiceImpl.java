package org.example.backend.service.impl;

import org.example.backend.dto.request.UserCreationRequest;
import org.example.backend.dto.request.UserUpdateRequest;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserService interface.
 * Provides business logic for user management operations including
 * CRUD operations with validation and business rules.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserServiceImpl with the specified user repository.
     * 
     * @param userRepository the user repository for data access
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user if one with the same name doesn't already exist.
     * 
     * @param request the user creation request containing user data
     * @return the created user
     * @throws IllegalArgumentException if the request is null or user already exists
     */
    @Override
    public User createUser(UserCreationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User creation request cannot be null");
        }

        User existingUser = userRepository.findByName(request.getName());
        if (existingUser != null) {
            throw new IllegalArgumentException("User with this name already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setNumberOfBorrowedBooks(0);
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param id the user ID
     * @return the user with the specified ID
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    @Override
    public User readUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found with id: " + id));
    }

    /**
     * Updates an existing user if the new name doesn't conflict with existing users.
     * 
     * @param id the user ID
     * @param request the user update request containing updated data
     * @return the updated user
     * @throws IllegalArgumentException if the request is null or user already exists
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    @Override
    public User updateUser(Long id, UserUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User update request cannot be null");
        }

        User user = readUser(id);
        User existingUserWithNewName = userRepository.findByName(request.getName());
        
        if (existingUserWithNewName != null && !existingUserWithNewName.getId().equals(id)) {
            throw new IllegalArgumentException("User with this name already exists");
        }

        user.setName(request.getName());
        
        if (request.getNumberOfBorrowedBooks() != null) {
            user.setNumberOfBorrowedBooks(request.getNumberOfBorrowedBooks());
        }
        
        return userRepository.save(user);
    }

    /**
     * Deletes a user if they have no borrowed books.
     * 
     * @param id the user ID
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     * @throws EntityBorrowedException if user has borrowed books
     */
    @Override
    public void deleteUser(Long id) {
        User user = readUser(id);
        
        if (user.getNumberOfBorrowedBooks() > 0) {
            throw new EntityBorrowedException("Cannot delete user with borrowed books");
        }
        
        userRepository.delete(user);
    }
}
