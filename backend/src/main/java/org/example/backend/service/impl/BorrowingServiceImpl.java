package org.example.backend.service.impl;

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
import org.example.backend.service.BorrowingService;
import org.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of BorrowingService interface.
 * Provides business logic for borrowing management operations including
 * borrowing books, returning books, and retrieving borrowing statistics.
 */
@Service
public class BorrowingServiceImpl implements BorrowingService {
    private final UserService userService;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;

    @Value("${borrowing.limit}")
    private int borrowingLimit;

    /**
     * Constructs a new BorrowingServiceImpl with the specified repositories.
     * 
     * @param userService the user service for data access
     * @param bookService the book service for data access
     * @param bookRepository the book repository for data access
     * @param borrowingRepository the borrowing repository for data access
     * @param userRepository the user repository for data access
     */
    public BorrowingServiceImpl(UserService userService, BookService bookService, BookRepository bookRepository, BorrowingRepository borrowingRepository, UserRepository userRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Borrows a book for a user if the book is available and user hasn't reached the limit.
     * 
     * @param userId the ID of the user borrowing the book
     * @param bookId the ID of the book being borrowed
     * @return the created borrowing transaction
     * @throws jakarta.persistence.EntityNotFoundException if book is not available or user limit exceeded
     */
    @Override
    public Borrowing borrowBook(Long userId, Long bookId) {
        User user = userService.readUser(userId);
        Book book = bookService.readBook(bookId);

        if(book.getAmount() == 0) throw new EntityNotFoundException("Book is not available");
        if(user.getBorrowings().size() >= borrowingLimit) throw new EntityNotFoundException("User's borrowing limit exceeded");

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);

        book.setAmount(borrowing.getBook().getAmount() - 1);
        book.setAmountOfBorrowedBooks(book.getAmountOfBorrowedBooks() + 1);
        bookRepository.save(book);
        user.setNumberOfBorrowedBooks(user.getNumberOfBorrowedBooks() + 1);
        userRepository.save(user);

        return borrowingRepository.save(borrowing);
    }

    /**
     * Gets all books borrowed by a specific user.
     * 
     * @param request the user information request containing the user's name
     * @return list of books borrowed by the user
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    @Override
    public List<Book> getBorrowedBooksByUserName(UserInformationRequest request) {
        User user = userRepository.findByName(request.getName());
        if(user == null) throw new EntityNotFoundException("User not found");
        return borrowingRepository.findBorrowedBooksByUserName(user.getName());
    }

    /**
     * Gets all distinct book titles that have been borrowed.
     * 
     * @return list of distinct borrowed book titles
     */
    @Override
    public List<BorrowedTitlesResponse> getDistinctBorrowedBooksTitles() {
        return borrowingRepository.findDistinctBorrowedBooksTitles();
    }

    /**
     * Gets statistics about borrowed books including title and count of borrowings.
     * 
     * @return list of borrowed books statistics
     */
    @Override
    public List<BorrowedBooksStatisticResponse> getDistinctBorrowedBooksTitlesAndCounts() {
        return borrowingRepository.findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies();
    }

    /**
     * Returns a borrowed book by removing the borrowing record and updating counts.
     * 
     * @param userId the ID of the user returning the book
     * @param bookId the ID of the book being returned
     * @throws jakarta.persistence.EntityNotFoundException if book, user, or borrowing is not found
     */
    @Override
    public void returnBook(Long userId, Long bookId) {
        Book book = bookService.readBook(bookId);
        User user = userService.readUser(userId);
        if (book == null) throw new EntityNotFoundException("Book is not available");
        if (user == null) throw new EntityNotFoundException("User is not available");
        List<Borrowing> borrowings = borrowingRepository.findBorrowingByUserIdAndBookId(userId, bookId);
        if (borrowings == null || borrowings.isEmpty()) throw new EntityNotFoundException("Borrowing is not available");

        Borrowing borrowing = borrowings.get(0);

        book.setAmountOfBorrowedBooks(book.getAmountOfBorrowedBooks() - 1);
        book.setAmount(book.getAmount() + 1);
        book.getBorrowings().remove(borrowing);
        bookRepository.save(book);

        user.setNumberOfBorrowedBooks(user.getNumberOfBorrowedBooks() - 1);
        user.getBorrowings().remove(borrowing);
        userRepository.save(user);

        borrowingRepository.delete(borrowing);
    }
}
