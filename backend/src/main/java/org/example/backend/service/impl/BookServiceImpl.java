package org.example.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.backend.dto.request.BookCreationRequest;
import org.example.backend.dto.request.BookUpdateRequest;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.exceptions.custom.EntityNullException;
import org.example.backend.model.Book;
import org.example.backend.repository.BookRepository;
import org.example.backend.service.BookService;
import org.springframework.stereotype.Service;

/**
 * Implementation of BookService interface.
 * Provides business logic for book management operations including
 * CRUD operations with validation and business rules.
 */
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    /**
     * Constructs a new BookServiceImpl with the specified book repository.
     * 
     * @param bookRepository the book repository for data access
     */
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new book or increases the amount if the book already exists.
     * 
     * @param request the book creation request containing book data
     * @return the created or updated book
     * @throws IllegalArgumentException if the request is null
     */
    @Override
    public Book createBook(BookCreationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Book creation request cannot be null");
        }

        Book existingBook = bookRepository.findByTitleAndAuthor(request.getTitle(), request.getAuthor());
        if (existingBook != null) {
            existingBook.setAmount(existingBook.getAmount() + 1);
            return bookRepository.save(existingBook);
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setAmount(1);
        book.setAmountOfBorrowedBooks(0);
        return bookRepository.save(book);
    }

    /**
     * Retrieves a book by its ID.
     * 
     * @param id the book ID
     * @return the book with the specified ID
     * @throws jakarta.persistence.EntityNotFoundException if book is not found
     */
    @Override
    public Book readBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Book not found with id: " + id));
    }

    /**
     * Updates an existing book.
     * 
     * @param id the book ID
     * @param request the book update request containing updated data
     * @return the updated book
     * @throws IllegalArgumentException if the request is null
     * @throws jakarta.persistence.EntityNotFoundException if book is not found
     */
    @Override
    public Book updateBook(Long id, BookUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Book update request cannot be null");
        }

        Book book = readBook(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        return bookRepository.save(book);
    }

    /**
     * Deletes a book if it has no borrowed copies.
     * 
     * @param id the book ID
     * @throws jakarta.persistence.EntityNotFoundException if book is not found
     * @throws EntityBorrowedException if book has borrowed copies
     */
    @Override
    public void deleteBook(Long id) {
        Book book = readBook(id);
        
        if (book.getAmountOfBorrowedBooks() > 0) {
            throw new EntityBorrowedException("Cannot delete book with borrowed copies");
        }
        
        bookRepository.delete(book);
    }
}
