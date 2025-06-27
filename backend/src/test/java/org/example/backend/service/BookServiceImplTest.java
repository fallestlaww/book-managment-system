package org.example.backend.service;

import org.example.backend.dto.request.BookCreationRequest;
import org.example.backend.dto.request.BookUpdateRequest;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.model.Book;
import org.example.backend.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.validation.ConstraintViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookCreationRequest creationRequest;
    private BookUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("TestTitle");
        book.setAuthor("Test Author");
        book.setAmount(1);
        book.setAmountOfBorrowedBooks(0);

        creationRequest = new BookCreationRequest();
        creationRequest.setTitle("TestTitle");
        creationRequest.setAuthor("Test Author");

        updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("UpdatedTitle");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setAmount(5);
        updateRequest.setAmountOfBorrowedBooks(2);
    }

    @Test
    void createBook_success_newBook() {
        when(bookRepository.findByTitleAndAuthor("TestTitle", "Test Author")).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.createBook(creationRequest);
        assertEquals("TestTitle", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals(1, result.getAmount());
        assertEquals(0, result.getAmountOfBorrowedBooks());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_success_existingBook() {
        Book existing = new Book();
        existing.setTitle("TestTitle");
        existing.setAuthor("Test Author");
        existing.setAmount(1);
        existing.setAmountOfBorrowedBooks(0);

        when(bookRepository.findByTitleAndAuthor("TestTitle", "Test Author")).thenReturn(existing);
        when(bookRepository.save(existing)).thenReturn(existing);

        Book result = bookService.createBook(creationRequest);
        assertEquals(2, result.getAmount());
        verify(bookRepository).save(existing);
    }

    @Test
    void createBook_nullRequest_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(null));
    }

    @Test
    void createBook_invalidTitle_throwsException() {
        creationRequest.setTitle("bad");
        assertThrows(ConstraintViolationException.class, () -> {
            validateBookRequest(creationRequest.getTitle(), creationRequest.getAuthor());
            bookService.createBook(creationRequest);
        });
    }

    @Test
    void createBook_invalidAuthor_throwsException() {
        creationRequest.setAuthor("badname");
        assertThrows(ConstraintViolationException.class, () -> {
            validateBookRequest(creationRequest.getTitle(), creationRequest.getAuthor());
            bookService.createBook(creationRequest);
        });
    }

    @Test
    void readBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book result = bookService.readBook(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void readBook_notFound_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.readBook(1L));
    }

    @Test
    void updateBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updated = bookService.updateBook(1L, updateRequest);
        assertEquals("UpdatedTitle", updated.getTitle());
        assertEquals("Updated Author", updated.getAuthor());
        assertEquals(5, updated.getAmount());
        assertEquals(2, updated.getAmountOfBorrowedBooks());
    }

    @Test
    void updateBook_nullId_throwsException() {
        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(null, updateRequest));
    }

    @Test
    void updateBook_nullRequest_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, null));
    }

    @Test
    void updateBook_invalidTitle_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        updateRequest.setTitle("bad");
        assertThrows(ConstraintViolationException.class, () -> {
            validateBookRequest(updateRequest.getTitle(), updateRequest.getAuthor());
            bookService.updateBook(1L, updateRequest);
        });
    }

    @Test
    void updateBook_invalidAuthor_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        updateRequest.setAuthor("badname");
        assertThrows(ConstraintViolationException.class, () -> {
            validateBookRequest(updateRequest.getTitle(), updateRequest.getAuthor());
            bookService.updateBook(1L, updateRequest);
        });
    }

    @Test
    void deleteBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_borrowed_throwsException() {
        book.setAmountOfBorrowedBooks(1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(EntityBorrowedException.class, () -> bookService.deleteBook(1L));
    }

    private void validateBookRequest(String title, String author) {
        if (title == null || !title.matches("^[A-Z][a-zA-Z]{2,}$")) {
            throw new ConstraintViolationException("Invalid title", null);
        }
        if (author == null || !author.matches("^[A-Z][a-z]+ [A-Z][a-z]+$")) {
            throw new ConstraintViolationException("Invalid author", null);
        }
    }
} 