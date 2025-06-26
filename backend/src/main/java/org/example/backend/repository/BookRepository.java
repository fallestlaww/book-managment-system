package org.example.backend.repository;

import org.example.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Book entity.
 * Provides data access methods for book operations.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Finds a book by its title and author.
     * 
     * @param title the title of the book
     * @param author the author of the book
     * @return the book with the specified title and author, or null if not found
     */
    Book findByTitleAndAuthor(String title, String author);
}
