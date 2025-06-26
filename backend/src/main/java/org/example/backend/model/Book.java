package org.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a book in the book management system.
 * Books can be borrowed by users and have multiple copies available.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@ToString
public class Book {
    /**
     * Unique identifier for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    /**
     * Title of the book. Must start with uppercase letter and contain at least 3 letters.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "Book's title is required")
    @NotNull(message = "Book's title is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]{2,}$", message = "Wrong book's title input")
    private String title;
    
    /**
     * Author of the book. Must be in format "FirstName LastName" with proper capitalization.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "Book author's name and surname is required")
    @NotNull(message = "Book author's name and surname is required")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Wrong book author's name and surname input")
    private String author;
    
    /**
     * Total number of copies of this book available in the library.
     * Must be non-negative.
     */
    @Min(0)
    private Integer amount;
    
    /**
     * Number of copies of this book currently borrowed by users.
     * Must be non-negative.
     */
    @Min(0)
    private Integer amountOfBorrowedBooks;
    
    /**
     * List of all borrowings associated with this book.
     * Mapped as one-to-many relationship with lazy loading.
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Borrowing> borrowings = new ArrayList<>();
}
