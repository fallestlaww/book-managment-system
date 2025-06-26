package org.example.backend.service;

import org.example.backend.dto.request.UserInformationRequest;
import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.model.Book;
import org.example.backend.model.Borrowing;

import java.util.List;

/**
 * Service interface for borrowing management operations.
 * Defines business logic methods for book borrowing and returning operations.
 */
public interface BorrowingService {
    /**
     * Borrows a book for a user.
     * 
     * @param userId the ID of the user borrowing the book
     * @param bookId the ID of the book being borrowed
     * @return the created borrowing transaction
     * @throws jakarta.persistence.EntityNotFoundException if book is not available or user limit exceeded
     */
    Borrowing borrowBook(Long userId, Long bookId);
    
    /**
     * Gets all books borrowed by a specific user.
     * 
     * @param request the user information request containing the user's name
     * @return list of books borrowed by the user
     * @throws jakarta.persistence.EntityNotFoundException if user is not found
     */
    List<Book> getBorrowedBooksByUserName(UserInformationRequest request);
    
    /**
     * Gets all distinct book titles that have been borrowed.
     * 
     * @return list of distinct borrowed book titles
     */
    List<BorrowedTitlesResponse> getDistinctBorrowedBooksTitles();
    
    /**
     * Gets statistics about borrowed books including title and count of borrowings.
     * 
     * @return list of borrowed books statistics
     */
    List<BorrowedBooksStatisticResponse> getDistinctBorrowedBooksTitlesAndCounts();
    
    /**
     * Returns a borrowed book.
     * 
     * @param userId the ID of the user returning the book
     * @param bookId the ID of the book being returned
     * @throws jakarta.persistence.EntityNotFoundException if book, user, or borrowing is not found
     */
    void returnBook(Long userId, Long bookId);
}
