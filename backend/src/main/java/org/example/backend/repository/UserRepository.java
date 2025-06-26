package org.example.backend.repository;

import org.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 * Provides data access methods for user operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their name.
     * 
     * @param name the name of the user to find
     * @return the user with the specified name, or null if not found
     */
    User findByName(String name);
}
