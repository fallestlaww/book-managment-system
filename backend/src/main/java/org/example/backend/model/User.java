package org.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user in the book management system.
 * Users can borrow books and have borrowing limits.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    /**
     * User's name. Must be a single word without spaces.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "User's name is required")
    @NotNull(message = "User's name is required")
    @Pattern(regexp = "^[^\\s]+$", message = "User's name must be a single word without spaces")
    private String name;
    
    /**
     * Date when the user became a member of the library.
     * Automatically set when the user is first persisted.
     */
    private Date membershipDate;
    
    /**
     * Number of books currently borrowed by the user.
     * Must be non-negative.
     */
    @Min(0)
    private Integer numberOfBorrowedBooks;
    
    /**
     * List of all borrowings associated with this user.
     * Mapped as one-to-many relationship with lazy loading.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Borrowing> borrowings = new ArrayList<>();

    /**
     * Automatically sets the membership date when the user is first persisted.
     * Called before the entity is persisted to the database.
     */
    @PrePersist
    public void prePersist() {
        membershipDate = new Date(System.currentTimeMillis());
    }
}
