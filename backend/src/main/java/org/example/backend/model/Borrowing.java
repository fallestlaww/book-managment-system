package org.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a borrowing transaction in the book management system.
 * Links a user with a book they have borrowed.
 */
@Entity
@Table(name = "borrowing")
@Getter
@Setter
@ToString
public class Borrowing {
    /**
     * Unique identifier for the borrowing transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    /**
     * The user who borrowed the book.
     * Many-to-one relationship with User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;
    
    /**
     * The book that was borrowed.
     * Many-to-one relationship with Book entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    private Book book;
}
