package org.example.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.backend.model.Book;

/**
 * Data Transfer Object for book information responses.
 * Contains book data formatted for API responses.
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookInformationResponse {
    /**
     * Title of the book.
     */
    String title;
    
    /**
     * Author of the book.
     */
    String author;
    
    /**
     * Total number of copies of this book available in the library.
     */
    int amount;
    
    /**
     * Number of copies of this book currently borrowed by users.
     */
    int amountOfBorrowedBooks;

    /**
     * Constructs a BookInformationResponse from a Book entity.
     * 
     * @param book the book entity to convert
     */
    public BookInformationResponse(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.amount = book.getAmount();
        this.amountOfBorrowedBooks = book.getAmountOfBorrowedBooks();
    }
}
