package org.example.backend.service;

import org.example.backend.dto.request.BookCreationRequest;
import org.example.backend.dto.request.BookUpdateRequest;
import org.example.backend.model.Book;

/**
 * Service interface for book management operations.
 * Defines business logic methods for book CRUD operations.
 */
public interface BookService {
    /**
     * Creates a new book or increases the amount if the book already exists.
     * 
     * @param request the book creation request containing book data
     * @return the created or updated book
     * @throws IllegalArgumentException if the request is null
     */
    Book createBook(BookCreationRequest request);
    
    /**
     * Retrieves a book by its ID.
     * 
     * @param id the book ID
     * @return the book with the specified ID
     * @throws jakarta.persistence.EntityNotFoundException if book is not found
     */
    Book readBook(Long id);
    
    /**
     * Updates an existing book.
     * 
     * @param id the book ID
     * @param request the book update request containing updated data
     * @return the updated book
     * @throws IllegalArgumentException if the request is null
     * @throws jakarta.persistence.EntityNotFoundException if book is not found
     */
    Book updateBook(Long id, BookUpdateRequest request);
    
    /**
     * Deletes a book.
     * 
     * @param id the book ID
     * @throws jakarta.persistence.EntityNotFoundException if book is not found or has borrowed copies
     */
    void deleteBook(Long id);
}
