package org.example.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.backend.dto.request.UserInformationRequest;
import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.model.Book;
import org.example.backend.model.Borrowing;
import org.example.backend.model.User;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.BorrowingRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.BookService;
import org.example.backend.service.UserService;
import org.example.backend.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    private User user;
    private Book book;
    private Borrowing borrowing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("TestUser");
        user.setNumberOfBorrowedBooks(0);
        user.setBorrowings(new ArrayList<>());

        book = new Book();
        book.setId(1L);
        book.setTitle("TestTitle");
        book.setAuthor("Test Author");
        book.setAmount(1);
        book.setAmountOfBorrowedBooks(0);
        book.setBorrowings(new ArrayList<>());

        borrowing = new Borrowing();
        borrowing.setId(1L);
        borrowing.setUser(user);
        borrowing.setBook(book);

        ReflectionTestUtils.setField(borrowingService, "borrowingLimit", 2);
    }

    @Test
    void borrowBook_success() {
        when(userService.readUser(1L)).thenReturn(user);
        when(bookService.readBook(1L)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);

        Borrowing result = borrowingService.borrowBook(1L, 1L);

        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
        verify(userRepository).save(any(User.class));
        verify(borrowingRepository).save(any(Borrowing.class));
    }

    @Test
    void borrowBook_bookNotAvailable_throwsException() {
        book.setAmount(0);
        when(userService.readUser(1L)).thenReturn(user);
        when(bookService.readBook(1L)).thenReturn(book);

        assertThrows(EntityNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_userLimitExceeded_throwsException() {
        user.getBorrowings().add(new Borrowing());
        user.getBorrowings().add(new Borrowing());
        when(userService.readUser(1L)).thenReturn(user);
        when(bookService.readBook(1L)).thenReturn(book);

        assertThrows(EntityNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void getBorrowedBooksByUserName_success() {
        UserInformationRequest request = new UserInformationRequest();
        request.setName("TestUser");
        when(userRepository.findByName("TestUser")).thenReturn(user);
        List<Book> books = List.of(book);
        when(borrowingRepository.findBorrowedBooksByUserName("TestUser")).thenReturn(books);

        List<Book> result = borrowingService.getBorrowedBooksByUserName(request);

        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void getBorrowedBooksByUserName_userNotFound_throwsException() {
        UserInformationRequest request = new UserInformationRequest();
        request.setName("Unknown");
        when(userRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> borrowingService.getBorrowedBooksByUserName(request));
    }

    @Test
    void getDistinctBorrowedBooksTitles_success() {
        List<BorrowedTitlesResponse> responses = List.of(new BorrowedTitlesResponse("TestTitle"));
        when(borrowingRepository.findDistinctBorrowedBooksTitles()).thenReturn(responses);

        List<BorrowedTitlesResponse> result = borrowingService.getDistinctBorrowedBooksTitles();

        assertEquals(1, result.size());
        assertEquals("TestTitle", result.get(0).getTitle());
    }

    @Test
    void getDistinctBorrowedBooksTitlesAndCounts_success() {
        List<BorrowedBooksStatisticResponse> responses = List.of(new BorrowedBooksStatisticResponse("TestTitle", 2L));
        when(borrowingRepository.findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies()).thenReturn(responses);

        List<BorrowedBooksStatisticResponse> result = borrowingService.getDistinctBorrowedBooksTitlesAndCounts();

        assertEquals(1, result.size());
        assertEquals("TestTitle", result.get(0).getTitle());
        assertEquals(2L, result.get(0).getAmountOfBorrowedBooks());
    }

    @Test
    void returnBook_success() {
        when(bookService.readBook(1L)).thenReturn(book);
        when(userService.readUser(1L)).thenReturn(user);
        when(borrowingRepository.findBorrowingByUserIdAndBookId(1L, 1L)).thenReturn(List.of(borrowing));
        when(bookRepository.save(book)).thenReturn(book);
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(borrowingRepository).delete(borrowing);

        assertDoesNotThrow(() -> borrowingService.returnBook(1L, 1L));
        verify(bookRepository).save(book);
        verify(userRepository).save(user);
        verify(borrowingRepository).delete(borrowing);
    }

    @Test
    void returnBook_bookNull_throwsException() {
        when(bookService.readBook(1L)).thenReturn(null);
        when(userService.readUser(1L)).thenReturn(user);

        assertThrows(EntityNotFoundException.class, () -> borrowingService.returnBook(1L, 1L));
    }

    @Test
    void returnBook_userNull_throwsException() {
        when(bookService.readBook(1L)).thenReturn(book);
        when(userService.readUser(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> borrowingService.returnBook(1L, 1L));
    }

    @Test
    void returnBook_borrowingNull_throwsException() {
        when(bookService.readBook(1L)).thenReturn(book);
        when(userService.readUser(1L)).thenReturn(user);
        when(borrowingRepository.findBorrowingByUserIdAndBookId(1L, 1L)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> borrowingService.returnBook(1L, 1L));
    }
} 