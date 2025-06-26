package org.example.backend.repository;

import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.model.Book;
import org.example.backend.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Borrowing entity.
 * Provides data access methods for borrowing operations and statistics.
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    /**
     * Finds all books borrowed by a specific user.
     * 
     * @param userName the name of the user
     * @return list of books borrowed by the user
     */
    @Query("SELECT b from Book b WHERE b.id IN (" +
            "SELECT br.book.id FROM Borrowing br " +
            "WHERE br.user.name = :userName)")
    List<Book> findBorrowedBooksByUserName(@Param("userName") String userName);
    
    /**
     * Finds all distinct book titles that have been borrowed.
     * 
     * @return list of distinct borrowed book titles
     */
    @Query("SELECT DISTINCT new org.example.backend.dto.response.BorrowedTitlesResponse(b.title) FROM Borrowing br JOIN br.book b")
    List<BorrowedTitlesResponse> findDistinctBorrowedBooksTitles();
    
    /**
     * Finds statistics about borrowed books including title and count of borrowings.
     * 
     * @return list of borrowed books statistics
     */
    @Query("SELECT DISTINCT new org.example.backend.dto.response.BorrowedBooksStatisticResponse(b.title, COUNT(br)) FROM Borrowing br JOIN br.book b GROUP BY b.title")
    List<BorrowedBooksStatisticResponse> findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies();
    
    /**
     * Finds borrowings by user ID and book ID.
     * 
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return list of borrowings matching the criteria
     */
    List<Borrowing> findBorrowingByUserIdAndBookId(Long userId, Long bookId);
}
